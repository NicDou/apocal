package com.jd.apocal.gateway.config;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jd.apocal.gateway.util.jackson2.DefaultSavedRequestMixin;
import com.jd.apocal.gateway.util.jackson2.OAuth2AccessTokenMixin;
import com.jd.apocal.gateway.util.jackson2.OAuth2AuthenticationMixin;
import com.jd.apocal.gateway.util.jackson2.OAuth2RequestMixin;
import com.jd.apocal.gateway.util.jackson2.UserDetailsImplMixin;
import com.jd.common.utils.UserDetailsImpl;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;


public class AuthJackson2Module extends SimpleModule {

  public AuthJackson2Module() {
    super(AuthJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
  }

  @Override
  public void setupModule(SetupContext context) {
    context.setMixInAnnotations(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);
    context.setMixInAnnotations(OAuth2Authentication.class, OAuth2AuthenticationMixin.class);
    context.setMixInAnnotations(OAuth2Request.class, OAuth2RequestMixin.class);
    context.setMixInAnnotations(UserDetailsImpl.class, UserDetailsImplMixin.class);
    context.setMixInAnnotations(DefaultSavedRequest.class, DefaultSavedRequestMixin.class);
  }
}