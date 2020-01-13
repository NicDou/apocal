package com.jd.apocal.model.feign;

import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.feign.fallback.FeignClientFallbackFactory;
import com.jd.common.vo.DictDetailVO;
import com.jd.common.vo.UserVO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author DOU
 * @date 2019-09-10 16:15
 */
@FeignClient(name = "apocal-portal", fallbackFactory = FeignClientFallbackFactory.class)
public interface UserService {

  @RequestMapping(method = RequestMethod.POST, value = "/auth/getCurrentUser.json")
  UserVO getCurrentUser(@RequestParam(Constant.HEADER_JD_TOKEN) String token);

  @RequestMapping(method = RequestMethod.POST, value = "/auth/getDirectUsersByOfficeId.json")
  List<UserVO> getUsersByDeptId(@RequestParam String deptId);

  @RequestMapping(method = RequestMethod.POST, value = "/auth/getDictDetails.json")
  List<DictDetailVO> getDictDetail(@RequestParam String dictKey);

}
