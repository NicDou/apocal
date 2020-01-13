package com.jd.apocal.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.model.entity.ModelApplication;
import com.jd.apocal.model.entity.ModelService;
import com.jd.apocal.model.entity.ServiceAuth;
import com.jd.common.vo.UserVO;
import java.util.List;
import java.util.Map;

public interface ModelApplicationService extends IService<ModelApplication> {

  void addApp(ModelApplication app);

  List<String> getStatistics(long id, int userType, long officeId,
      Map<String, Integer> result);

  List<ModelService> getModelService(Map<String, Object> params);

  List<UserVO> getAuthorizedUsersByServiceId(String deptId, String serviceId,
      long userId);

  void addAuth(String serviceId, List<ServiceAuth> serviceAuth);

  void deleteAuth(String modelId);

}
