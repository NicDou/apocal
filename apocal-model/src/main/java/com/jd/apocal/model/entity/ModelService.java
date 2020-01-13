package com.jd.apocal.model.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelService {

  private String modelId;

  private String modelName;

  private String modelStatus;

  private List<ModelApplication> services;

}
