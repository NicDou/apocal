package com.jd.apocal.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("service")
public class ModelApplication {

  @TableId(type = IdType.AUTO)
  private int id;
  private String uuid;
  private String name;
  private String modelImage;
  private String description;
  private String cpu;
  private String modelId;
  private String memory;
  private String type;
  private String replicas;
  private String status;
  private String creator;
  private String deptName;
  private Date deployDate;
  private String cron;
  private String dataSource;
  private String podName;
  private long userId;
  private long deptId;
  @TableField(exist = false)
  private int currentUserType;
  @TableField(exist = false)
  private long currentUserId;
  @TableField(exist = false)
  private String readable;
  @TableField(exist = false)
  private String writable;

}
