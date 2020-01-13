package com.jd.apocal.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.apocal.model.entity.Task;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMapper extends BaseMapper<Task> {

  List<Task> getAllTasks();

  //int selectCountByMap(@Param("params") Map<String, Object> params);

}