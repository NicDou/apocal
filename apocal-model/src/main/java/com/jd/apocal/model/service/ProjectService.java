package com.jd.apocal.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.model.entity.Project;
import java.util.List;

public interface ProjectService extends IService<Project> {

  List<Project> getSuccessfullyBuiltProjectsList();

  boolean canDelete(String id);

}
