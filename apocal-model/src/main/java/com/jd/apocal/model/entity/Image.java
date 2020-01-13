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
@TableName("image")
public class Image {

  @TableId(type = IdType.AUTO)
  private int id;
  private String name;
  private String status;
  private String labels;
  private Date createTime;
  private String projectId;
  private String log;

}
