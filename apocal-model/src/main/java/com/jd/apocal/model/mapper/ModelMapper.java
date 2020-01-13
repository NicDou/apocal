package com.jd.apocal.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.apocal.model.entity.Model;
import com.jd.apocal.model.entity.ModelUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelMapper extends BaseMapper<Model> {

  String selectMetrics(String id);

  List<String> selectImages();

  List<ModelUser> selectModelUser(@Param(value = "modelId") String modelId);

  void addAuth(@Param(value = "modelId") String modelId,
      @Param(value = "list") List<ModelUser> modelUsers);

  void deleteAuth(@Param(value = "modelId") String modelId);

  int selectModelApp(@Param(value = "modelId") String modeId);

}