package com.jd.apocal.model.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.core.cli.Cli;
import com.jd.apocal.model.core.cli.CliResult;
import com.jd.apocal.model.entity.ExpGroup;
import com.jd.apocal.model.entity.Experiment;
import com.jd.apocal.model.entity.Image;
import com.jd.apocal.model.entity.ModelSet;
import com.jd.apocal.model.feign.UserService;
import com.jd.apocal.model.service.ExpGroupService;
import com.jd.apocal.model.service.ExperimentService;
import com.jd.apocal.model.service.ImageService;
import com.jd.apocal.model.service.ModelSetService;
import com.jd.apocal.model.utils.CharacterUtils;
import com.jd.apocal.model.utils.FileUtil;
import com.jd.apocal.model.utils.UuidUtil;
import com.jd.common.exception.ServiceException;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.DictDetailVO;
import com.jd.common.vo.PageVO;
import com.jd.common.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@Slf4j
@RestController
@RequestMapping("/experiments")
public class ExperimentController {

  @Autowired
  private ExperimentService experimentSvc;

  @Autowired
  private ExpGroupService expGroupSvc;

  @Autowired
  private ImageService imgSvc;

  @Autowired
  private UserService userService;

  @Autowired
  private ModelSetService modelSetService;

  @Value("${service.namespace}")
  private String namespace;

  @Value("${scripts.home}")
  private String scriptsHome;

  @Autowired
  private Cli cli;

  @Value("${export.dir}")
  private String root;

  @ApiOperation(value = "分页获取实验列表", notes = "分页获取实验列表")
  @RequestMapping(value = "/getExperimentList.json")
  public RestServiceResult<List<Experiment>> getExperimentList(PageVO pageVO) {

    return new RestServiceResult<List<Experiment>>();
  }

  @ApiOperation(value = "添加实验", notes = "添加实验")
  @RequestMapping(value = "/addExperiment.json")
  public RestServiceResult<Boolean> addExperiment(UserVO user, @Valid Experiment exp,
      String yaml) {
    String expId = UuidUtil.getInstance().getUUid();
    exp.setCreatorId(user.getId());
    exp.setCreator(user.getUsername());
    exp.setDeptId(user.getOfficeId());
    exp.setDeptName(user.getOfficeName());
    exp.setId(expId);
    String expGroupId = exp.getExpGroupId();
    ExpGroup group = expGroupSvc.getById(expGroupId);

    if (group == null) {

      throw new ServiceException("实验组:" + expGroupId + "不存在");

    }

    String projectId = group.getProjectId();

    exp.setProjectId(projectId);

    exp.setLocation(
        root + Constant.WORKFLOW_EXP_GROUP_PATH_NAME + expGroupId + File.separator + "EXP_"
            + expId);
    exp.setType("M");
    exp.setName("EX-MANUAL-" + exp.getName());
    experimentSvc.save(exp);

    expGroupSvc.generateYaml(exp.getLocation(), yaml);

    return new RestServiceResult<Boolean>(true);
  }

  @ApiOperation(value = "运行实验", notes = "运行实验")
  @RequestMapping(value = "/runExperiment.json")
  public RestServiceResult<Boolean> runExperiment(@RequestParam String id) {

    Experiment exp = experimentSvc.getById(id);

    if (!Constant.STATUS_RUNNING.equals(exp.getStatus())) {

      String projectId = exp.getProjectId();

      Image image = imgSvc.getLatestImage(projectId);

      if (image == null) {

        log.error("project:{}无可用镜像", projectId);

        throw new ServiceException("项目:" + projectId + "无可用镜像");
      }

      String expDir = exp.getLocation();

      String k8sJobYamlLocation = expDir + File.separator + Constant.WORKFLOW_K8S_JOB_YAML_NAME;

      String controllerYamlLocation = expDir.replace(root + Constant.WORKFLOW_EXP_GROUP_PATH_NAME,
          Constant.DOCKER_EXP_PATH_NAME) + File.separator + Constant.WORKFLOW_CONTROLLER_YAML_NAME;

      String randomString = CharacterUtils.getRandomString(6);
      String name = id + "-" + randomString;
      FileUtil.generateJobYamlFormTemplate(scriptsHome + File.separator + "job_template.yaml",
          k8sJobYamlLocation,
          name, image.getName(), controllerYamlLocation);

      String command =
          scriptsHome + File.separator + "deploy.sh" + " " + k8sJobYamlLocation + " " + namespace;

      log.info("executing command:{}", command);

      CliResult execute = cli.execute(command);

      int status = execute.getStatus();

      List<String> outputs = execute.getOutput();

      log.info("status:{}", status);

      log.info("output:{}", outputs);

      Experiment updateExperiment = Experiment.builder().id(id)
          .deployLog(JSON.toJSONString(outputs))
          .image(image.getName()).flag(randomString).status("P").build();

      experimentSvc.updateById(updateExperiment);
    }
    return new RestServiceResult<Boolean>(true);

  }

  @ApiOperation(value = "实验详情", notes = "实验详情")
  @RequestMapping(value = "/detail.json")
  @Transactional
  public RestServiceResult<Experiment> detail(@RequestParam String id) {

    Experiment exp = experimentSvc.getById(id);
    return new RestServiceResult<Experiment>(exp);
  }

  @ApiOperation(value = "删除实验", notes = "删除实验")
  @RequestMapping(value = "/del.json")
  public RestServiceResult<Boolean> delete(@RequestParam String id) throws IOException {

    Experiment exp = experimentSvc.getById(id);
    String expDir = exp.getLocation();

    // 删除实验目录
    FileUtils.deleteDirectory(new File(expDir));
    // 删除实验记录
    experimentSvc.removeById(id);

    return new RestServiceResult<Boolean>(true);
  }

  @ApiOperation(value = "获取实验控制文件内容", notes = "获取实验控制文件内容")
  @RequestMapping(value = "/getExperimentYaml.json")
  public RestServiceResult<List<String>> getExperimentYaml(@RequestParam String id)
      throws Throwable {

    Experiment exp = experimentSvc.getById(id);

    String expYamlFile =
        exp.getLocation() + File.separator + Constant.WORKFLOW_CONTROLLER_YAML_NAME;

    List<String> lines = null;

    if (new File(expYamlFile).exists()) {

      lines = FileUtils.readLines(new File(expYamlFile), "UTF-8");

    } else {

      throw new ServiceException("未找到控制文件");
    }

    return new RestServiceResult<List<String>>(lines);

  }

  @SuppressWarnings("unchecked")
  @ApiOperation(value = "模型性能", notes = "模型性能")
  @RequestMapping(value = "/performance.json")
  public RestServiceResult<Map<String, Object>> performance(@RequestParam String id) {

    Experiment exp = experimentSvc.getById(id);

    String metrics = exp.getMetrics();

    Map<String, Object> map = Maps.newHashMap();

    if (StringUtils.isNotBlank(metrics)) {

      map = JSON.parseObject(metrics, Map.class);

    }

    return new RestServiceResult<Map<String, Object>>(map);
  }

  @ApiOperation(value = "导出模型", notes = "导出模型")
  @RequestMapping(value = "/exports.json")
  public RestServiceResult<Boolean> exports(UserVO user, @RequestParam String id) {

    Experiment exp = experimentSvc.getById(id);

    String modelZip = exp.getLocation() + Constant.MODEL_ZIP_FILE_PATH_NAME;

    String modelSetId = UuidUtil.getInstance().getUUid();

    String archiveDir = root + Constant.WORKFLOW_MODEL_PATH_NAME + modelSetId;

    if (new File(modelZip).exists()) {

      try {

        FileUtils.copyFileToDirectory(new File(modelZip), new File(archiveDir));

      } catch (IOException e) {

        log.error("导出模型文件失败", e);

        throw new ServiceException("导出模型文件失败" + e.getMessage());
      }

      ModelSet modelSet = ModelSet.builder().id(modelSetId).creator(user.getUsername())
          .creatorId(user.getId())
          .deptId(user.getOfficeId()).deptName(user.getOfficeName()).location(archiveDir)
          .name(Constant.MODEL_ZIP_FILE_NAME).build();

      modelSetService.save(modelSet);

    } else {

      throw new ServiceException("模型文件:【" + modelZip + "】不存在");

    }

    return new RestServiceResult<>(true);
  }

  @ApiOperation(value = "模型指标", notes = "模型指标")
  @RequestMapping(value = "/index.json")
  public RestServiceResult<List<DictDetailVO>> index(@RequestParam String dictKey) {

    return new RestServiceResult<>(userService.getDictDetail(dictKey));

  }
}
