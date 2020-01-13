package com.jd.apocal.portal.model.dto;


import com.jd.apocal.portal.model.entity.Member;
import java.util.List;
import lombok.Data;


@Data
public class MemberDTO extends Member {

  /**
   * 角色ID
   */
  private List<Integer> role;

  /**
   * 新密码
   */
  private String newpassword1;
}
