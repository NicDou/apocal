package com.jd.apocal.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.model.entity.Task;
import com.jd.apocal.model.mapper.TaskMapper;
import com.jd.apocal.model.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

  @Autowired
  private TaskMapper taskMapper;

  @Override
  public List<Task> getAllTasks() {
    return taskMapper.getAllTasks();
  }


}
