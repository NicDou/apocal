package com.jd.apocal.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.entity.ExpGroup;
import com.jd.apocal.model.entity.Experiment;
import com.jd.apocal.model.entity.Task;
import com.jd.apocal.model.mapper.ExpGroupMapper;
import com.jd.apocal.model.mapper.ExperimentMapper;
import com.jd.apocal.model.mapper.TaskMapper;
import com.jd.apocal.model.service.ExpGroupService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExpGroupServiceImpl extends ServiceImpl<ExpGroupMapper, ExpGroup> implements
    ExpGroupService {

  @Autowired
  private ExpGroupMapper expGroupMapper;

  @Autowired
  private ExperimentMapper expMapper;

  @Autowired
  private TaskMapper taskMapper;

  @Override
  public void generateYaml(String dir, String yaml) {

    File file = new File(dir);

    if (!file.exists()) {

      file.mkdirs();
    }

    String[] lines = yaml.split("\n");

    try {

      BufferedWriter out = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(dir + File.separator
              + Constant.WORKFLOW_CONTROLLER_YAML_NAME), "UTF-8"));

      for (String line : lines) {
        out.write(line + "\n");
      }

      out.close();

    } catch (IOException e) {

      log.error("控制文件生成失败", e);

    }

  }

  @Override
  public int getCountByMap(Map<String, Object> params) {

    return expGroupMapper.selectCountByMap(params);

  }

  @Override
  public boolean canDelete(String id) {

    Map<String, Object> of = ImmutableMap.<String, Object>of("group_id", id, "status", "R");

    Map<String, Object> of2 = ImmutableMap.<String, Object>of("group_id", id, "status", "P");

    return (taskMapper.selectCount(new QueryWrapper<Task>().allEq(of)) == 0
        && expMapper.selectCount(new QueryWrapper<Experiment>().allEq(of2)) == 0);

  }
}
