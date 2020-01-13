package com.jd.apocal.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private long id;

  private String username;

  private String realName;

  private String mobilePhone;

  private int userType;

  private long officeId;

  private String officeName;

  private boolean canRead;

  private boolean canWrite;

}
