package com.jd.apocal.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.model.entity.Image;
import com.jd.apocal.model.mapper.ImageMapper;
import com.jd.apocal.model.service.ImageService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

  @Autowired
  ImageMapper imageMapper;

  @Override
  public Image getLatestImage(String projectId) {

    Image image = imageMapper.selectLatestImage(projectId);

    return image;
  }

  @Override
  public int getCountByMap(Map<String, Object> params) {

    return imageMapper.selectCountByMap(params);
  }
}
