package com.jd.apocal.model.vo;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DOU
 * @date 2019-10-09 14:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentVO {

  private String id;
  @NotNull
  private String name;
  private String projectName;
  private String expGroupName;
  private String image;
  private String type;
  private String expGroupId;
  private String projectId;
  private String jobId;
  private String jobName;
  private String labels;
  private String creator;
  private long creatorId;
  private String deptName;
  private String status;
  private long deptId;
  private Date startTime;
  private Date endTime;
  private String deployLog;
  private String flag;
  private String location;
  private String metrics;
  private String category;
  private String statistics;
}
