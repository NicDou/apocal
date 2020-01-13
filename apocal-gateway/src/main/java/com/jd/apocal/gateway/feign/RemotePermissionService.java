package com.jd.apocal.gateway.feign;


import com.jd.apocal.gateway.entity.vo.PermissionVO;
import com.jd.apocal.gateway.feign.fallback.FeignClientFallbackFactory;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "apocal-portal", fallbackFactory = FeignClientFallbackFactory.class/*fallback = RemotePermissionServiceFallbackImpl.class*/)
public interface RemotePermissionService {

  /**
   * 通过角色名查询菜单
   *
   * @param role 角色名称
   * @return 菜单列表
   */
  @GetMapping(value = "/rest/permission/findPermissionByRole/{role}")
  Set<PermissionVO> findPermissionByRole(@PathVariable("role") String role);
}
