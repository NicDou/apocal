package com.jd.apocal.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jd.apocal.model.entity.ModelApplication;
import com.jd.apocal.model.entity.ModelService;
import com.jd.apocal.model.entity.ServiceAuth;
import com.jd.apocal.model.feign.UserService;
import com.jd.apocal.model.mapper.ModelAppMapper;
import com.jd.apocal.model.service.ModelApplicationService;
import com.jd.common.vo.UserVO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelApplicationServiceImpl extends
    ServiceImpl<ModelAppMapper, ModelApplication> implements
    ModelApplicationService {

  @Autowired
  ModelAppMapper modelAppMapper;

  @Autowired
  UserService userService;

  @Override
  public void addApp(ModelApplication app) {

    this.save(app);

  }

  @Override
  public List<String> getStatistics(long id, int userType, long officeId,
      Map<String, Integer> result) {
    Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);

    // 部门管理员
    if (userType == 4) {
      params.put("deptId", officeId);

    } else if (userType == 1) {
      params.put("userId", id);
    }
    // params 无参数 就是admin 用户所有的都有

    int modelCount = modelAppMapper.selectMoldeByParam(params);

    List<String> apps = modelAppMapper.selectServiceByParam(params);

    result.put("upServiceCount", 0);
    result.put("downServiceCount", 0);
    result.put("modelCount", modelCount);
    result.put("alertCount", 0);

    return apps;

  }

  @Override
  public List<ModelService> getModelService(Map<String, Object> params) {

    return modelAppMapper.selectModelService(params);

  }

  @Override
  public List<UserVO> getAuthorizedUsersByServiceId(String deptId,
      String serviceId, long userId) {

    List<UserVO> users = Lists.newArrayList();

    List<UserVO> deptUsers = userService.getUsersByDeptId(deptId);

    users = deptUsers.stream()
        .filter(o -> (o.getUserType() == 1 && o.getId() != userId))
        .collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(users)) {

      List<ServiceAuth> serviceAuths = modelAppMapper
          .selectSertviceAuth(serviceId);

      if (!CollectionUtils.isEmpty(serviceAuths)) {

        for (UserVO user : users) {

          for (ServiceAuth serviceAuth : serviceAuths) {

            if (user.getId() == serviceAuth.getUserId()) {

              user.setCanWrite("Y".equals(serviceAuth
                  .getCanWrite()) ? true : false);

              user.setCanRead("Y".equals(serviceAuth.getCanRead()) ? true
                  : false);

              break;

            }

          }

        }
      }
    }

    return users;
  }

  @Override
  public void addAuth(String serviceId, List<ServiceAuth> serviceAuth) {

    modelAppMapper.addAuth(serviceId, serviceAuth);
  }

  @Override
  public void deleteAuth(String serviceId) {

    modelAppMapper.deleteAuth(serviceId);

  }

}
