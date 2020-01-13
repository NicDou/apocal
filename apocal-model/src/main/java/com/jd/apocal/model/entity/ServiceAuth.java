package com.jd.apocal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("service_user")
public class ServiceAuth {

  private long userId;
  private String serviceId;
  @TableField(value = "readable")
  private String canRead;
  @TableField(value = "writable")
  private String canWrite;

}
