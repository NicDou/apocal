package com.jd.apocal.model.feign.fallback;

import com.jd.apocal.model.feign.UserService;
import com.jd.common.vo.DictDetailVO;
import com.jd.common.vo.UserVO;
import feign.hystrix.FallbackFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignClientFallbackFactory implements FallbackFactory<UserService> {

  @Override
  public UserService create(Throwable cause) {

    return new UserService() {

      @Override
      public UserVO getCurrentUser(String token) {
        log.error("调用{}异常:{}", "getCurrentUser", token, cause);
        return null;
      }

      @Override
      public List<UserVO> getUsersByDeptId(String deptId) {

        log.error("调用{}异常:{}", "getUsersByDeptId", deptId, cause);
        return null;
      }

      @Override
      public List<DictDetailVO> getDictDetail(String dictKey) {
        log.error("调用{}异常:{}", "getDictDetail", dictKey, cause);
        return null;
      }

    };
  }

}
