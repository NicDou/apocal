package com.jd.apocal.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.model.entity.Task;
import java.util.List;

public interface TaskService extends IService<Task> {

  /**
   * 获取所有任务列表
   *
   * @return
   */
  List<Task> getAllTasks();


}
