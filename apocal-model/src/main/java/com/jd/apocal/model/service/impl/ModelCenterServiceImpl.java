package com.jd.apocal.model.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.jd.apocal.model.core.doc.ModelDocument;
import com.jd.apocal.model.core.metas.deploy.DeployYml;
import com.jd.apocal.model.core.metas.deploy.Resources;
import com.jd.apocal.model.entity.Model;
import com.jd.apocal.model.entity.ModelUser;
import com.jd.apocal.model.feign.UserService;
import com.jd.apocal.model.mapper.ModelMapper;
import com.jd.apocal.model.service.ModelCenterService;
import com.jd.common.vo.UserVO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class ModelCenterServiceImpl extends
    ServiceImpl<ModelMapper, Model> implements ModelCenterService {

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  UserService userService;


  @Transactional
  public Model addModel(ModelDocument doc) {

    Map<String, Object> modelYml = doc.getModelYml();

    Map<String, Object> modelInfo = (Map<String, Object>) modelYml.get("model");
    Map<String, Object> base = (Map<String, Object>) modelYml.get("base");

    Map<String, Object> extInfo = (Map<String, Object>) modelYml.get("ext");

    DeployYml deployYml = doc.getDeployYml();

    Resources resources = deployYml.getResources();

    Map<String, Object> in = doc.getDataSpecYml().get(0);

    Map<String, Object> out = doc.getDataSpecYml().get(1);

    String programLanguage = (String) modelInfo.get("programLanguage");

    Model model = Model.builder().id(doc.getDocId()).name((String) base.get("name"))
        .creator(doc.getUserName())
        .location(doc.getZipLocation()).category((String) modelInfo.get("class"))
        .library((String) modelInfo.get("library")).programLanguage(programLanguage)
        .description((String) extInfo.get("description")).cpu(resources.getCpu())
        .memory(resources.getMemory())
        .metric(JSON.toJSONString(doc.getMetric())).input(JSON.toJSONString(in.get("inData")))
        .output(JSON.toJSONString(out.get("outData"))).deptId(doc.getOfficeId())
        .userId(doc.getUserId())
        .deptName(doc.getOfficeName()).build();

    this.save(model);

    return model;

  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> getMetrics(String id) {

    String metrics = modelMapper.selectMetrics(id);

    Map<String, Object> map = JSON.parseObject(metrics, Map.class);

    return map;

  }

  @Override
  public List<UserVO> getAuthorizedUsersByModelId(String deptId, String modelId, long userId) {

    List<UserVO> users = Lists.newArrayList();

    // 模型所属部门下的普通人员,排除所属人员
    List<UserVO> deptUsers = userService.getUsersByDeptId(deptId);

    users = deptUsers.stream().filter(o -> (o.getUserType() == 1 && o.getId() != userId))
        .collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(users)) {

      List<ModelUser> modelUsers = modelMapper.selectModelUser(modelId);

      if (!CollectionUtils.isEmpty(modelUsers)) {

        for (UserVO user : users) {

          for (ModelUser modelUser : modelUsers) {

            if (user.getId() == modelUser.getUserId()) {

              user.setCanWrite("Y".equals(modelUser.getCanWrite()) ? true : false);

              user.setCanRead("Y".equals(modelUser.getCanRead()) ? true : false);

              break;

            }

          }

        }
      }
    }
    return users;
  }

  @Override
  public void addAuth(String modelId, List<ModelUser> modelUsers) {
    modelMapper.addAuth(modelId, modelUsers);

  }

  @Override
  public void deleteAuth(String modelId) {

    modelMapper.deleteAuth(modelId);

  }

  @Override
  public boolean existApp(String id) {

    return modelMapper.selectModelApp(id) > 0;
  }

}
