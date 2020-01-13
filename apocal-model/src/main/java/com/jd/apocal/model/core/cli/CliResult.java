package com.jd.apocal.model.core.cli;

import java.util.List;
import lombok.Data;

@Data
public class CliResult {

  private int status;
  private String error;
  private List<String> output;

}
