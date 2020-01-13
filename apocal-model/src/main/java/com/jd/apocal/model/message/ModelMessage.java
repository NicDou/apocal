package com.jd.apocal.model.message;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ModelMessage implements Serializable {

  private static final long serialVersionUID = -7342120696096166945L;

  private String modelId;

  private String baseImage;

  private String modelImage;

  private String modelDir;

  private String renamedModelImage;

  private String programLanguage;

  private String scriptsHome;

  private String runtimeImage;

  private String harbor;

}
