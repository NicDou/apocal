package com.jd.apocal.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.model.entity.ExpGroup;
import java.util.Map;

public interface ExpGroupService extends IService<ExpGroup> {

  /**
   * 生成实验组目录并拷贝项目的控制文件
   *
   * @param dir
   * @param yaml
   */
  void generateYaml(String dir, String yaml);

  int getCountByMap(Map<String, Object> params);

  boolean canDelete(String id);

}
