package com.jd.apocal.auth.service;


import com.jd.common.utils.UserDetailsImpl;
import com.jd.common.vo.vo.MemberVO;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

  //@Resource
  //private MemberService memberService;

  @Override
  public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
    //  MemberVO memberVo = memberService.findMemberByMembername(username);

    MemberVO memberVo = new MemberVO();

    memberVo.setPassword("123456");
    memberVo.setMemberId(1067629916525072386L);
    memberVo.setDelFlag("0");
    memberVo.setMembername("vole");

    memberVo.setRoleVoList(new ArrayList<>());
    if (memberVo != null) {
      return new UserDetailsImpl(memberVo);
    }
    throw new UsernameNotFoundException("error username");
  }
}
