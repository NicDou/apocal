package com.jd.apocal.model.message;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ModelAppMessage implements Serializable {

  private static final long serialVersionUID = -544194748436147915L;

  private String deployConfigHome;

  private String namespace;

  private String memory;

  private String cpu;

  private String image;

  private String replicas;

  private String deployName;

  private String specName;

  private String scriptsHome;

  private int id;

  private String uUid;

}
