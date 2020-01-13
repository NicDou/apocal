package com.jd.apocal.model.entity;

import lombok.Data;

@Data
public class K8sJobStatus {

  // "Failed" || "Complete" || ""
  private String type;

  private String startTime;

  private String completionTime;

}
