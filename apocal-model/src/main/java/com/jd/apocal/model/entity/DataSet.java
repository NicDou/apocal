package com.jd.apocal.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
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

@TableName("data_set")
public class DataSet {

  @TableId(type = IdType.INPUT)
  private String id;
  @TableField(condition = SqlCondition.LIKE)
  private String name;

  private String location;

  private String creator;

  @TableField(condition = SqlCondition.LIKE)
  private String labels;

  private Date createTime;

  private Long creatorId;

  private Long deptId;

  @TableField(condition = SqlCondition.LIKE)
  private String deptName;

}
