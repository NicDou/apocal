package com.jd.apocal.model.controller;

import com.google.common.collect.Maps;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.core.cli.Cli;
import com.jd.apocal.model.core.cli.CliResult;
import com.jd.apocal.model.entity.ModelService;
import com.jd.apocal.model.service.ModelApplicationService;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.UserVO;
import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/modelIndex")
public class ModelIndexController {

  @Autowired
  private ModelApplicationService mas;

  @Value("${scripts.home}")
  private String scriptsHome;

  @Value("${service.namespace}")
  private String namespace;

  @Autowired
  private Cli cli;

  @RequestMapping(value = "/status.json")
  public RestServiceResult<Map<String, Integer>> getStatistics(UserVO user) {

    Map<String, Integer> result = Maps.newHashMapWithExpectedSize(2);

    List<String> ids = mas
        .getStatistics(user.getId(), user.getUserType(), user.getOfficeId(), result);

    Map<String, Integer> podStatus = getPodStatus(ids);

    result.putAll(podStatus);

    return new RestServiceResult<Map<String, Integer>>(result);

  }

  @RequestMapping(value = "/relationGraphics.json")
  public RestServiceResult<List<ModelService>> modelRelationGraphics(UserVO user) {

    long id = user.getId();

    long officeId = user.getOfficeId();

    int userType = user.getUserType();

    Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);

    // 部门管理员
    if (userType == 4) {

      params.put("deptId", officeId);

      // 普通用户
    } else if (userType == 1) {

      params.put("userId", id);

    }

    List<ModelService> modelService = mas.getModelService(params);

    return new RestServiceResult<List<ModelService>>(modelService);

  }

  public Map<String, Integer> getPodStatus(List<String> ids) {

    Map<String, Integer> map = Maps.newHashMapWithExpectedSize(2);

    int upServiceCount = 0;

    int downServiceCount = 0;

    for (String id : ids) {

      CliResult execute = cli.execute(scriptsHome + File.separator + "sdep_status.sh" + " "
          + Constant.DEPLOY_NAME_PREFIX + id + " " + namespace);

      log.debug("===========Seldon Deployemnt model-{} status :{}", id, execute.getOutput());

      List<String> output = execute.getOutput();

      if (!CollectionUtils.isEmpty(output)) {

        String status = output.get(0);

        if ("Available".equals(status)) {

          upServiceCount++;

        } else {
          // 创建中的也是算异常的？？
          downServiceCount++;
        }

      } else {

        downServiceCount++;
      }

    }

    map.put("upServiceCount", upServiceCount);

    map.put("downServiceCount", downServiceCount);

    return map;

  }
}
