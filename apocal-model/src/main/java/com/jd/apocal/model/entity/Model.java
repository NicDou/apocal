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
@TableName("model")
public class Model {

  @TableId(type = IdType.INPUT)
  private String id;
  private String name;
  private String library;
  private String category;
  private String modelImage;
  private String label;
  private String status;
  private String creator;
  private String deptName;
  private Date createTime;
  @TableField(value = "language")
  private String programLanguage;
  private String description;
  private String cpu;
  private String memory;
  private String metric;
  private String location;
  private String input;
  private String output;
  private String errorMessage;
  private long userId;
  private long deptId;
  private String baseImage;

  @TableField(exist = false)
  private int currentUserType;
  @TableField(exist = false)
  private long currentUserId;
  @TableField(exist = false)
  private String readable;
  @TableField(exist = false)
  private String writable;

}