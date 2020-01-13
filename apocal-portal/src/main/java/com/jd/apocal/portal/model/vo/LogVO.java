package com.jd.apocal.portal.model.vo;


import com.jd.apocal.portal.model.entity.SysLog;
import java.io.Serializable;
import lombok.Data;

@Data
public class LogVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private SysLog sysLog;
  private String username;
}
