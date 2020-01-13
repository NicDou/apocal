package com.jd.apocal.model.message;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectMessage implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -3902128256762983856L;

  private String id;

  private String baseImage;

  private String scriptsHome;

  private String projectLocation;

  private String imageRepository;

}
