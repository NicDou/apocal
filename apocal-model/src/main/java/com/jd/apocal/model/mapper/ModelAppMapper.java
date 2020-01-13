package com.jd.apocal.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.apocal.model.entity.ModelApplication;
import com.jd.apocal.model.entity.ModelService;
import com.jd.apocal.model.entity.ServiceAuth;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelAppMapper extends BaseMapper<ModelApplication> {

  List<String> selectServiceByParam(@Param(value = "params") Map<String, Object> params);

  int selectMoldeByParam(@Param(value = "params") Map<String, Object> params);

  List<ModelService> selectModelService(@Param(value = "params") Map<String, Object> params);

  List<ServiceAuth> selectSertviceAuth(@Param(value = "serviceId") String serviceId);

  void addAuth(@Param(value = "serviceId") String serviceId,
      @Param(value = "list") List<ServiceAuth> serviceAuth);

  void deleteAuth(@Param(value = "serviceId") String serviceId);

}
