package com.jd.apocal.model.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.core.cli.Cli;
import com.jd.apocal.model.core.cli.CliResult;
import com.jd.apocal.model.entity.Model;
import com.jd.apocal.model.entity.ModelApplication;
import com.jd.apocal.model.entity.ServiceAuth;
import com.jd.apocal.model.feign.UserService;
import com.jd.apocal.model.message.ModelAppMessage;
import com.jd.apocal.model.service.ModelApplicationService;
import com.jd.apocal.model.service.ModelCenterService;
import com.jd.apocal.model.utils.UuidUtil;
import com.jd.common.exception.ServiceException;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.PageVO;
import com.jd.common.vo.UserVO;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/modelApplication")
public class ModelApplicationController {

  @Autowired
  private ModelApplicationService mas;

  @Autowired
  private ModelCenterService mcs;

  @Autowired
  private JmsMessagingTemplate sender;

  @Autowired
  private UserService userService;

  @Autowired
  private Cli cli;

  @Value("${dashboard.monitor.url}")
  private String monitorUrl;

  @Value("${dashboard.log.url}")
  private String logUrl;

  @Value("${service.namespace}")
  private String namespace;

  @Value("${ambassador.url}")
  private String ambassador;

  @Value("${export.dir}")
  private String root;

  @Value("${scripts.home}")
  private String scriptsHome;

  @Value("${base.image.repository}")
  private String baseImageRepository;


  @RequestMapping(value = "/getListByConditionForPage.json")
  public RestServiceResult<Page<ModelApplication>> getListByCondition(UserVO user,
      PageVO page) {

    long officeId = user.getOfficeId();

    int userType = user.getUserType();

    long id = user.getId();

    Map params = Maps.newHashMapWithExpectedSize(2);

    // 非超级用户即部门管理员和普通用户看自己直属部门的
    if (userType != 3) {
      params.put("deptId", officeId);

    }

    Page<ModelApplication> apps = (Page<ModelApplication>) mas
        .page(new Page<ModelApplication>(page.getCurrentPage(), page.getPageSize()),
            new QueryWrapper<ModelApplication>().eq(userType != 3, "dept_id", officeId));

    for (ModelApplication app : apps.getRecords()) {

      app.setCurrentUserType(userType);

      app.setCurrentUserId(id);
    }

    return new RestServiceResult<Page<ModelApplication>>();
  }

  @RequestMapping(value = "/add.json")
  public RestServiceResult<String> add(UserVO user, ModelApplication app) {

    String uUid = UuidUtil.getInstance().getUUid();

    app.setUuid(uUid);

    app.setMemory(app.getMemory() + "Mi");

    app.setUserId(user.getId());

    app.setDeptId(user.getOfficeId());

    app.setDeptName(user.getOfficeName());

    app.setCreator(user.getUsername());

    Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);

    param.put("modelImage", app.getModelImage());

    List<Model> byMap = (List<Model>) mcs.listByMap(param);

    app.setModelId(byMap.get(0).getId());

    mas.addApp(app);

    String deployName = Constant.DEPLOY_NAME_PREFIX + app.getId();

    String specName = Constant.SPEC_NAME_PREFIX + app.getId();

    ModelAppMessage message = ModelAppMessage.builder().id(app.getId()).uUid(uUid).cpu(app.getCpu())
        .memory(app.getMemory()).replicas(app.getReplicas()).namespace(namespace)
        .deployName(deployName)
        .specName(specName).image(app.getModelImage())
        .deployConfigHome(root + File.separator + uUid)
        .scriptsHome(scriptsHome).build();

    sender.convertAndSend(Constant.MESSAGE_DESTINATION_SERVICE_DEPLOYMENT, message);

    return new RestServiceResult<>();
  }

  @RequestMapping(value = "/detail.json")
  public RestServiceResult<ModelApplication> detail(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    ModelApplication app = mas.getById(id);

    return new RestServiceResult<ModelApplication>(app);
  }

  @RequestMapping(value = "/test.json")
  public RestServiceResult<String> test(
      @RequestParam(value = "id", defaultValue = "", required = true) String id,
      @RequestParam String param) throws Exception {

    String url = MessageFormat.format(ambassador, namespace, Constant.DEPLOY_NAME_PREFIX + id);

    // String result = HttpAgent.getInstance().post(url, param);
    String result = null;
    return new RestServiceResult<>(result);
  }

  @RequestMapping(value = "/getServiceUrl.json")
  public RestServiceResult<String> getServiceUrl(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    return new RestServiceResult<>(
        MessageFormat.format(ambassador, namespace, Constant.DEPLOY_NAME_PREFIX + id));
  }

  @RequestMapping(value = "/delete.json")
  public RestServiceResult<String> delete(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    mas.removeById(id);

    return new RestServiceResult<>();
  }

  @RequestMapping(value = "/pause.json")
  public RestServiceResult<String> pause(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    ModelApplication app = mas.getById(id);

    String deployConfigHome = root + File.separator + app.getUuid();

    CliResult execute = cli
        .execute(scriptsHome + File.separator + "delete.sh " + deployConfigHome + File.separator
            + "deployment.json" + " " + namespace);

    log.debug("delete seldon deployment model-{}:{}", id, execute.getOutput());

    return new RestServiceResult<>();
  }

  @RequestMapping(value = "/reDeploy.json")
  public RestServiceResult<String> reDeploy(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    ModelApplication app = mas.getById(id);

    String deployName = Constant.DEPLOY_NAME_PREFIX + app.getId();

    String specName = Constant.SPEC_NAME_PREFIX + app.getId();

    ModelAppMessage message = ModelAppMessage.builder().id(app.getId()).uUid(app.getUuid())
        .cpu(app.getCpu())
        .memory(app.getMemory()).replicas(app.getReplicas()).namespace(namespace)
        .deployName(deployName)
        .specName(specName).image(app.getModelImage())
        .deployConfigHome(root + File.separator + app.getUuid())
        .scriptsHome(scriptsHome).build();
    sender.convertAndSend(Constant.MESSAGE_DESTINATION_SERVICE_DEPLOYMENT, message);
    return new RestServiceResult<>();
  }

  @RequestMapping(value = "/getServiceLog.json")
  public RestServiceResult<Object> getServiceLog(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    String podName = null;

    CliResult execute = cli.execute(scriptsHome + File.separator + "status.sh " + namespace);

    int status = execute.getStatus();

    if (status == 1) {

      List<String> output = execute.getOutput();

      for (String out : output) {

        if (out.contains(Constant.SPEC_NAME_PREFIX + id)) {

          podName = out.substring(0, out.indexOf(":"));

          break;

        }

      }

    } else {

      throw new ServiceException(execute.getOutput().toString());

    }

    JSONArray logs = null;

    if (podName != null) {

      String url = MessageFormat.format(logUrl, namespace, podName);

      // String result = HttpAgent.getInstance().sendHttpGet(url, null);
      String result = null;
      // 停止服务后 会删除pod
      if (!result.contains("not found")) {

        JSONObject parseObject = JSON.parseObject(result);

        logs = (JSONArray) parseObject.get("logs");

      }
    }

    return new RestServiceResult<>(logs);
  }

  @RequestMapping(value = "/resource.json")
  public RestServiceResult<Map<String, Object>> resource(@RequestParam String id) {

    String podName = null;

    CliResult execute = cli.execute(scriptsHome + File.separator + "status.sh " + namespace);

    int status = execute.getStatus();

    if (status == 1) {

      List<String> output = execute.getOutput();

      for (String out : output) {

        if (out.contains(Constant.SPEC_NAME_PREFIX + id)) {

          podName = out.substring(0, out.indexOf(":"));

          break;

        }

      }

    } else {

      throw new ServiceException(execute.getOutput().toString());

    }

    String url = MessageFormat.format(monitorUrl, namespace, podName);

    Map<String, Object> map = new HashMap<String, Object>(2);

    //String result = HttpAgent.getInstance().sendHttpGet(url, null);
    String result = null;
    // 停止服务后 会删除pod
    if (!result.contains("not found")) {

      JSONObject parseObject = JSON.parseObject(result);

      JSONArray metrics = (JSONArray) parseObject.get("metrics");

      if (metrics != null && metrics.size() == 2) {

        JSONObject cpuUsageMetric = (JSONObject) metrics.get(0);

        JSONObject memoryUsageMetric = (JSONObject) metrics.get(1);

        map.put("cpuUsage", cpuUsageMetric.get("metricPoints"));
        map.put("memoryUsage", memoryUsageMetric.get("metricPoints"));

      }

    } else {

      ArrayList<Object> newArrayList = Lists.newArrayList();
      map.put("cpuUsage", newArrayList);
      map.put("memoryUsage", newArrayList);

    }

    return new RestServiceResult<>(map);
  }

  @RequestMapping(value = "/getPodStatus.json")
  public RestServiceResult<String> getPodStatus(
      @RequestParam(value = "id", defaultValue = "", required = true) int id) {

    String staus = null;

    CliResult execute = cli.execute(scriptsHome + File.separator + "sdep_status.sh" + " "
        + Constant.DEPLOY_NAME_PREFIX + id + " " + namespace);

    if (execute.getStatus() == 1) {

      List<String> output = execute.getOutput();

      log.debug("seldon deployment model-{} status:{}", id, output);

      if (!CollectionUtils.isEmpty(output)) {

        String back = output.get(0);

        if (back.contains("not found")) {

          staus = "down";

        } else {

          staus = back;

        }

      }

    } else {

      throw new ServiceException(execute.getOutput().toString());

    }
    return new RestServiceResult<>(staus);

  }

  @RequestMapping(value = "/auth.json")
  public RestServiceResult<String> auth(@RequestParam String serviceId, @RequestParam String auth) {

    List<ServiceAuth> auths = JSONArray.parseArray(auth, ServiceAuth.class);

    mas.deleteAuth(serviceId);

    if (!CollectionUtils.isEmpty(auths)) {

      mas.addAuth(serviceId, auths);

    }

    return new RestServiceResult<String>();

  }


}
