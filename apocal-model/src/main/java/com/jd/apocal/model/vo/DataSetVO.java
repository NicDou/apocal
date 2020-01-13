package com.jd.apocal.model.vo;

import com.jd.apocal.model.entity.DataSet;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DataSetVO extends DataSet {

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createStartTime;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createEndTime;

}
