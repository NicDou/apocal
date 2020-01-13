package com.jd.apocal.auth.config;

import com.jd.apocal.auth.component.mobile.MobileSecurityConfigurer;
import com.jd.apocal.auth.util.OauthLogoutHandler;
import com.jd.apocal.auth.util.message.AuthAuthenticationFailureHandler;
import com.jd.apocal.auth.util.message.AuthAuthenticationSuccessHandler;
import com.jd.common.bean.config.FilterIgnorePropertiesConfig;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 * spring security配置 在WebSecurityConfigurerAdapter不拦截oauth要开放的资源
 */
@EnableWebSecurity
public class SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  @Resource
  private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;
  @Autowired
  private MobileSecurityConfigurer mobileSecurityConfigurer;
  @Autowired
  OauthLogoutHandler oauthLogoutHandler;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
        http
//                        .exceptionHandling().accessDeniedHandler(new AuthAccessDeniedHandler())
//                        .authenticationEntryPoint(new AuthAuthenticationEntryPoint())
//                        .and()
            .formLogin().loginPage("/authentication/require")
            .loginProcessingUrl("/authentication/form")
            .successHandler(new AuthAuthenticationSuccessHandler())
            .failureHandler(new AuthAuthenticationFailureHandler())
            .and()
            .logout().logoutUrl("/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(oauthLogoutHandler)
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("vole")
            .and()
            .authorizeRequests();
    // filterIgnorePropertiesConfig.getUrls().forEach(url -> registry.antMatchers(url).permitAll());
    // registry.anyRequest().authenticated()
    registry.anyRequest().permitAll()
        .and()
        .csrf().disable();

    //配置手机号获取token
    http.apply(mobileSecurityConfigurer);


  }


  /**
   * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
   *
   * @return 认证管理对象
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  // password 方案一：明文存储，用于测试，不能用于生产

  @Bean
  PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  // password 方案二：用 BCrypt 对密码编码
  //@Bean
  //    PasswordEncoder passwordEncoder(){
  //        return new BCryptPasswordEncoder();
  //    }

  /*  @Bean
    PasswordEncoder passwordEncoder(){
        DelegatingPasswordEncoder delegatingPasswordEncoder =
                (DelegatingPasswordEncoder)  PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return delegatingPasswordEncoder;


    }
    */

}
