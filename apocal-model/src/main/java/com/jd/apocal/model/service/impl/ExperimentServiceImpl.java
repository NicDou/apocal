package com.jd.apocal.model.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.core.cli.Cli;
import com.jd.apocal.model.core.cli.CliResult;
import com.jd.apocal.model.core.doc.ModelDocument;
import com.jd.apocal.model.entity.Experiment;
import com.jd.apocal.model.entity.K8sJobStatus;
import com.jd.apocal.model.mapper.ExperimentMapper;
import com.jd.apocal.model.service.ExperimentService;
import com.jd.apocal.model.utils.DateUtils;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExperimentServiceImpl extends ServiceImpl<ExperimentMapper, Experiment> implements
    ExperimentService {

  @Autowired
  private ExperimentMapper expMapper;

  @Autowired
  private Cli cli;

  @Value("${service.namespace}")
  private String namespace;
  @Value("${scripts.home}")
  private String scriptsHome;

  @Override
  public int getCountByMap(Map<String, Object> param) {

    return expMapper.selectCountByMap(param);
  }

  @PostConstruct
  private void startUpdateSchedule() {

    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {

        log.error("============ Updating Experiment =================");

        updateEx();

      }
    }, 10, 10, TimeUnit.SECONDS);

  }

  private void updateEx() {

    Map<String, Object> params = ImmutableMap.<String, Object>of("status", Constant.STATUS_RUNNING);

    List<Experiment> exp = (List<Experiment>) listByMap(params);

    if (exp != null && exp.size() > 0) {

      exp.stream().forEach(e -> {

        String k8sJobName = e.getId() + "-" + e.getFlag();

        K8sJobStatus status = getK8sJobStatusByJobName(k8sJobName);

        log.info("experiment:{},status:{}", e.getId(), status);

        if (status == null) {

          Experiment build = Experiment.builder().id(e.getId()).status(Constant.STATUS_UNKNOWN)
              .build();

          updateById(build);

        } else {

          String type = status.getType();

          String endTime = status.getCompletionTime();

          String startTime = status.getStartTime();

          Date start = null;

          Date end = null;

          if (StringUtils.isNotBlank(startTime)) {

            start = DateUtils.convertToDate(startTime, "yyyy-MM-dd'T'HH:mm:ss'Z'");

          }
          if (StringUtils.isNotBlank(endTime)) {

            end = DateUtils.convertToDate(endTime, "yyyy-MM-dd'T'HH:mm:ss'Z'");
          }

          if ("Complete".equals(type)) {

            Experiment build = Experiment.builder().id(e.getId()).status(Constant.STATUS_SUCCESS)
                .endTime(end).startTime(start).build();

            updateById(build);

            // 更新metric和 类型 与状态分开update 是避免解析出错导致状态更新失败

            try {
              ModelDocument modelDocument = new ModelDocument(
                  new File(e.getLocation() + File.separator
                      + "data" + File.separator + Constant.MODEL_PROJECT_NAME), null, 0, 0, null,
                  null);

              Map<String, Map<String, List<String[]>>> metric = modelDocument.getMetric();

              Map<String, Object> modelYml = modelDocument.getModelYml();

              Map<String, Object> modelInfo = (Map<String, Object>) modelYml.get("model");

              String category = (String) modelInfo.get("class");

              Experiment build2 = Experiment.builder().id(e.getId())
                  .metrics(JSON.toJSONString(metric))
                  .category(category).build();

              updateById(build2);

              // 删除完成的job

              String command =
                  scriptsHome + File.separator + "delete_job.sh" + " " + k8sJobName + " "
                      + namespace;

              log.info("executing command:{}", command);

              CliResult execute = cli.execute(command);

              log.info("delete complete job output status:{}", execute.getStatus());

              log.info("delete complete job output:{}", execute.getOutput());

            } catch (Exception ex) {

              log.error("获取目录:{}下模型数据失败", e.getLocation() + File.separator + "data" + File.separator
                  + Constant.MODEL_PROJECT_NAME, ex);
            }

          } else if ("Failed".equals(type)) {

            Experiment build = Experiment.builder().id(e.getId()).status(Constant.STATUS_FAILURE)
                .startTime(start).build();

            updateById(build);

          }

        }

      });
    }

  }

  @Override
  public K8sJobStatus getK8sJobStatusByJobName(String k8sJobName) {

    String command =
        scriptsHome + File.separator + "job_status.sh" + " " + k8sJobName + " " + namespace;

    log.info("executing command:{}", command);

    CliResult execute = cli.execute(command);

    int status = execute.getStatus();

    List<String> output = execute.getOutput();

    log.info("getK8sJobStatusByJobName output status:{}", status);

    log.info("getK8sJobStatusByJobName output:{}", output);
    K8sJobStatus job = null;
    if (status != -2 && output != null && output.size() > 0 && !output.get(0).contains("Error")) {

      String jobStr = output.get(0);

      try {

        job = JSON.parseObject(jobStr, K8sJobStatus.class);

      } catch (Exception e) {

        log.error("获取实验:{}状态失败", k8sJobName, e);

      }

    } else {

      log.error("获取实验:{}状态失败", k8sJobName);

    }

    return job;
  }

  @Override
  public List<Experiment> getExpsByGroupId(String groupId) {

    return expMapper.selectExpsByGroupId(groupId);
  }

}
