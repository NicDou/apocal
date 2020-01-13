package com.jd.apocal.gateway.entity.vo;

import com.jd.apocal.gateway.entity.SysLog;
import java.io.Serializable;
import lombok.Data;

@Data
public class LogVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private SysLog sysLog;
  private String username;
}
