package com.jd.apocal.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("project")
public class Project {

  @TableId(type = IdType.INPUT)
  private String id;
  private String name;
  private String version;
  private String kind;
  private String creator;
  private long creatorId;
  private String deptName;
  private long deptId;
  private String labels;
  private String cpu;
  private String memory;
  private String storage;
  private String params;
  private String location;
  private String status;
  private Date createTime;

}
