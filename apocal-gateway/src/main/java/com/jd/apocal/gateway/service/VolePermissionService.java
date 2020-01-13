package com.jd.apocal.gateway.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 *
 */
public interface VolePermissionService {

  /**
   * 判断请求是否有权限
   *
   * @param request        HttpServletRequest
   * @param authentication 认证信息
   * @return 是否有权限
   */
  boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
