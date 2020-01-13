package com.jd.apocal.model.bean.resolver;

import com.jd.apocal.model.feign.UserService;
import com.jd.common.constants.SecurityConstants;
import com.jd.common.vo.UserVO;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Token转化MemberVo
 */
@Slf4j
@Component
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

  @Autowired
  private UserService userService;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterType().equals(UserVO.class);
  }

  /**
   * @param methodParameter       入参集合
   * @param modelAndViewContainer vo 和 view
   * @param nativeWebRequest      web相关
   * @param webDataBinderFactory  入参解析
   * @return 包装对象
   * @throws Exception exception
   */
  @Override
  public Object resolveArgument(MethodParameter methodParameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
    HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
    String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
    if (StringUtils.isBlank(token)) {
      log.warn("resolveArgument error token is empty");
      return null;
    } else {

      UserVO currentUser = userService.getCurrentUser(token);

      log.info("resolveArgument userVO :{} ", currentUser);

      return currentUser;
    }

  }

}
