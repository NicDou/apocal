package com.jd.apocal.model.core.metas.deploy;

import lombok.Data;

@Data
public class RunTime {

  private Model model;

  private Deployment deployment;

  private Service Service;

}
