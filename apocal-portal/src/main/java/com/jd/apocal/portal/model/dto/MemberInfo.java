package com.jd.apocal.portal.model.dto;

import com.jd.apocal.portal.model.entity.Member;
import java.io.Serializable;
import lombok.Data;


@Data
public class MemberInfo implements Serializable {

  /**
   * 用户基本信息
   */
  private Member member;
  /**
   * 权限标识集合
   */
  private String[] permissions;

  /**
   * 角色集合
   */
  private String[] roles;
}
