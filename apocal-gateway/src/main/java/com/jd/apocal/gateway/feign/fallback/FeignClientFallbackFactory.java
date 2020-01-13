package com.jd.apocal.gateway.feign.fallback;

import com.jd.apocal.gateway.entity.vo.PermissionVO;
import com.jd.apocal.gateway.feign.RemotePermissionService;
import feign.hystrix.FallbackFactory;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author DOU
 * @date 2019-11-15 10:53
 */
@Slf4j
@Component
public class FeignClientFallbackFactory implements FallbackFactory<RemotePermissionService> {

  @Override
  public RemotePermissionService create(Throwable cause) {

    return new RemotePermissionService() {

      @Override
      public Set<PermissionVO> findPermissionByRole(String role) {
        log.error("调用{}异常:{}", "findPermissionByRole", role, cause);
        return null;
      }

    };

  }
}
