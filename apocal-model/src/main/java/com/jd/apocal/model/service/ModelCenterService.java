package com.jd.apocal.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.model.core.doc.ModelDocument;
import com.jd.apocal.model.entity.Model;
import com.jd.apocal.model.entity.ModelUser;
import com.jd.common.vo.UserVO;
import java.util.List;
import java.util.Map;

public interface ModelCenterService extends IService<Model> {

  Model addModel(ModelDocument modelDocument);

  Map<String, Object> getMetrics(String id);

  List<UserVO> getAuthorizedUsersByModelId(String deptId, String modelId,
      long userId);

  void addAuth(String modelId, List<ModelUser> modelUsers);

  void deleteAuth(String modelId);

  boolean existApp(String id);

}
