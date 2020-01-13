package com.jd.apocal.auth.component.mobile;

import com.jd.apocal.auth.fegin.MemberService;
import javax.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 手机号登录配置入口
 * <p>
 * 用于用手机号直接获取access_token url (/mobile/cookie?mobile=XXX) header(client & secret)
 */
@Component
public class MobileSecurityConfigurer extends
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  @Resource
  private AuthenticationSuccessHandler mobileLoginSuccessHandler;
  @Resource
  private MemberService memberService;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
    mobileAuthenticationFilter
        .setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
    mobileAuthenticationFilter.setAuthenticationSuccessHandler(mobileLoginSuccessHandler);

    MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
    mobileAuthenticationProvider.setMemberService(memberService);
    http.authenticationProvider(mobileAuthenticationProvider)
        .addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
