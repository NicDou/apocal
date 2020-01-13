package com.jd.apocal.model.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.entity.ExpGroup;
import com.jd.apocal.model.entity.Experiment;
import com.jd.apocal.model.service.ExpGroupService;
import com.jd.apocal.model.service.ExperimentService;
import com.jd.apocal.model.service.TaskService;
import com.jd.apocal.model.utils.UuidUtil;
import com.jd.common.exception.ServiceException;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.PageVO;
import com.jd.common.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/expGroups")
@Slf4j
public class ExpGroupController {

  @Autowired
  private ExpGroupService expGroupSvc;

  @Autowired
  private ExperimentService expSvc;

  @Autowired
  private TaskService taskSvc;


  @Value("${export.dir}")
  private String root;

  @ApiOperation(value = "分页获取实验组列表", notes = "分页获取实验组列表")
  @RequestMapping(value = "/getExpGroupsList.json")
  public RestServiceResult<List<ExpGroup>> getExpGroupsList(PageVO page) {

    return new RestServiceResult<List<ExpGroup>>();
  }

  @ApiOperation(value = "统计实验组运行信息", notes = "统计实验组运行信息")
  @RequestMapping(value = "/getExpGroupStatistics.json")
  public RestServiceResult<Map<String, Integer>> getExpStatistics(@RequestParam String groupId) {

    int success = expSvc
        .getCountByMap(
            ImmutableMap.<String, Object>of("groupId", groupId, "status", Constant.STATUS_SUCCESS));
    int failure = expSvc
        .getCountByMap(
            ImmutableMap.<String, Object>of("groupId", groupId, "status", Constant.STATUS_FAILURE));

    int running = expSvc
        .getCountByMap(
            ImmutableMap.<String, Object>of("groupId", groupId, "status", Constant.STATUS_RUNNING));

    Map<String, Integer> statistics = ImmutableMap.<String, Integer>of("running", running,
        "success", success,
        "failure", failure);

    return new RestServiceResult<Map<String, Integer>>(statistics);
  }

  @ApiOperation(value = "添加实验组", notes = "添加实验组")
  @RequestMapping(value = "/add.json")
  public RestServiceResult<Boolean> add(UserVO user, ExpGroup expgroup, String yaml) {

    String uUid = UuidUtil.getInstance().getUUid();
    expgroup.setCreatorId(user.getId());
    expgroup.setCreator(user.getUsername());
    expgroup.setDeptId(user.getOfficeId());
    expgroup.setDeptName(user.getOfficeName());
    expgroup.setId(uUid);
    expGroupSvc.save(expgroup);
    expGroupSvc
        .generateYaml(root + Constant.WORKFLOW_EXP_GROUP_PATH_NAME + uUid + File.separator, yaml);

    return RestServiceResult.rest(true);
  }

  @ApiOperation(value = "编辑实验组", notes = "编辑实验组")
  @RequestMapping(value = "/update.json")
  public RestServiceResult<Boolean> update(ExpGroup expgroup, String yaml) {

    expGroupSvc.updateById(expgroup);
    expGroupSvc.generateYaml(
        root + Constant.WORKFLOW_EXP_GROUP_PATH_NAME + expgroup.getId() + File.separator,
        yaml);
    return RestServiceResult.rest(true);
  }

  @ApiOperation(value = "删除实验组", notes = "删除实验组")
  @RequestMapping(value = "/delete.json")
  public RestServiceResult<Boolean> delete(@RequestParam String id) throws IOException {

    boolean canDelete = expGroupSvc.canDelete(id);

    if (canDelete) {

      // 删除实验组目录
      FileUtils.deleteDirectory(new File(root + Constant.WORKFLOW_EXP_GROUP_PATH_NAME + id));
      // 删除实验组记录
      expGroupSvc.removeById(id);

      Map<String, Object> params = ImmutableMap.<String, Object>of("groupId", id);

      // 删除实验组下实验
      expSvc.removeByMap(params);
      // 删除实验组下任务
      taskSvc.removeByMap(params);

      return RestServiceResult.rest(true);

    } else {

      throw new ServiceException("无法删除,该实验组下有未完成的实验或已启动的任务");

    }

  }

  @ApiOperation(value = "获取实验组控制文件内容", notes = "获取实验组控制文件内容")
  @RequestMapping(value = "/getExGroupYaml.json")
  public RestServiceResult<List<String>> getExGroupYaml(@RequestParam String id)
      throws IOException {

    String groupYamlFile = root + Constant.WORKFLOW_EXP_GROUP_PATH_NAME + id + File.separator
        + Constant.WORKFLOW_CONTROLLER_YAML_NAME;

    List<String> lines = null;

    if (new File(groupYamlFile).exists()) {

      lines = FileUtils.readLines(new File(groupYamlFile), "UTF-8");

    } else {

      throw new ServiceException("未找到控制文件");
    }

    return new RestServiceResult<List<String>>(lines);
  }

  @ApiOperation(value = "单条实验组信息", notes = "单条实验组信息")
  @RequestMapping(value = "/getDetail.json")
  public RestServiceResult<ExpGroup> getDetail(@RequestParam String id) {

    ExpGroup group = expGroupSvc.getById(id);

    return new RestServiceResult<ExpGroup>(group);
  }

  @ApiOperation(value = "模型指标排序", notes = "模型指标排序")
  @RequestMapping(value = "/index.json")
  public RestServiceResult<List<Experiment>> index(@RequestParam String groupId, String index) {

    List<Experiment> exps = expSvc.list(new QueryWrapper<Experiment>().eq("group_id", groupId));

    // 排除没有指定指标的
    List<Experiment> collect = exps.stream()
        .filter(f -> StringUtils.isNotBlank(f.getMetrics())
            && JSON.parseObject(f.getMetrics()).containsKey("Train_" + index + ".csv"))
        .collect(Collectors.toList());

    // 赋值指标字段
    collect.stream().forEach(experiment -> {

      try {

        String metrics = experiment.getMetrics();

        JSONObject json = JSON.parseObject(metrics);

        if (json.containsKey("Train_" + index + ".csv")) {

          JSONObject indexOj = (JSONObject) json.get("Train_" + index + ".csv");

          JSONArray contents = (JSONArray) indexOj.get("content");

          JSONArray content = (JSONArray) contents.get(0);

          String indexValue = (String) content.get(0);

          experiment.setStatistics(indexValue);

        }
        experiment.setMetrics(null);

      } catch (Exception e) {

        log.error("获取实验指:{}指标出错", experiment.getId(), e);

      }

    });

    // 排除指标字段赋值失败的

    List<Experiment> collect2 = collect.stream().filter(f -> f.getStatistics() != null)
        .collect(Collectors.toList());

    // 排序
    Collections.sort(collect2, (o1, o2) -> Double
        .compare(Double.valueOf(o2.getStatistics()), Double.valueOf(o1.getStatistics())));

    return new RestServiceResult<>(collect2);
  }


}
