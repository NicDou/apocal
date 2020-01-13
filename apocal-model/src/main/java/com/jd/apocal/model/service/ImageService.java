package com.jd.apocal.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.model.entity.Image;
import java.util.Map;

public interface ImageService extends IService<Image> {

  Image getLatestImage(String projectId);

  int getCountByMap(Map<String, Object> params);

}
