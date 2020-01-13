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
@TableName("job")
public class Task {

  @TableId(type = IdType.INPUT)
  private String id;
  private String name;
  @TableField(exist = false)
  private String projectName;
  @TableField(exist = false)
  private String expGroupName;
  private String expGroupId;
  private String projectId;
  private String labels;
  private String cron;
  private String status;
  private String creator;
  private long creatorId;
  private String deptName;
  private long deptId;
  private String location;
  private Date createTime;
  @TableField(exist = false)
  private String controllerYamlLocation;

}
