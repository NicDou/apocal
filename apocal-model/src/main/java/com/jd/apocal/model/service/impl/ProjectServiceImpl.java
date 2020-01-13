package com.jd.apocal.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.jd.apocal.model.entity.Experiment;
import com.jd.apocal.model.entity.Project;
import com.jd.apocal.model.entity.Task;
import com.jd.apocal.model.mapper.ExperimentMapper;
import com.jd.apocal.model.mapper.ProjectMapper;
import com.jd.apocal.model.mapper.TaskMapper;
import com.jd.apocal.model.service.ProjectService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements
    ProjectService {

  @Autowired
  private ProjectMapper projectMapper;
  @Autowired
  private ExperimentMapper expMapper;

  @Autowired
  private TaskMapper taskMapper;

  @Override
  public List<Project> getSuccessfullyBuiltProjectsList() {

    Map<String, Object> params = new HashMap<String, Object>();

    params.put("status", "S");

    return projectMapper.selectByMap(params);
  }

  @Override
  public boolean canDelete(String id) {

    Map<String, Object> of = ImmutableMap.<String, Object>of("projectId", id, "status", "R");

    Map<String, Object> of2 = ImmutableMap.<String, Object>of("projectId", id, "status", "P");

    return (taskMapper.selectCount(new QueryWrapper<Task>().allEq(of)) == 0
        && expMapper.selectCount(new QueryWrapper<Experiment>().allEq(of2)) == 0);
  }
}
