package com.jd.apocal.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("experiment")
public class Experiment {

  @TableId(type = IdType.INPUT)
  private String id;

  @NotBlank(message = "实验名称不能为空")
  @Length(min = 1, max = 2, message = "实验名称长度必须在{min}和{max}之间")
  private String name;

  private String image;
  private String type;
  private String expGroupId;
  private String projectId;
  private String jobId;
  private String labels;
  private String status;
  private Date startTime;
  private Date endTime;
  private String deployLog;
  private String flag;
  private String location;
  @TableField(value = "model_metrics")
  private String metrics;
  private String category;


  private long creatorId;
  private String creator;
  private long deptId;
  private String deptName;


  @TableField(exist = false)
  private String jobName;
  @TableField(exist = false)
  private String statistics;
  @TableField(exist = false)
  private String projectName;
  @TableField(exist = false)
  private String expGroupName;


}
