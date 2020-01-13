package com.jd.apocal.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.apocal.model.entity.Image;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMapper extends BaseMapper<Image> {

  Image selectLatestImage(@Param(value = "projectId") String projectId);

  int selectCountByMap(@Param("params") Map<String, Object> params);

}