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
@TableName("model_set")
public class ModelSet {

  @TableId(type = IdType.INPUT)
  private String id;
  private String name;
  private String location;
  private String creator;
  private Date createTime;
  private Long creatorId;
  private Long deptId;
  private String deptName;

}
