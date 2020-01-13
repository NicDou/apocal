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
@TableName("model_user")
public class ModelUser {

  private long userId;
  private String modelId;
  @TableField(value = "readable")
  private String canRead;
  @TableField(value = "writable")
  private String canWrite;

}
