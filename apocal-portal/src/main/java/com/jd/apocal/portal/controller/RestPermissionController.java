package com.jd.apocal.portal.controller;

import com.jd.apocal.portal.model.vo.PermissionVO;
import com.jd.apocal.portal.service.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/permission")
public class RestPermissionController {

  @Autowired
  private PermissionService permissionService;


  /**
   * 通过角色查询权限信息
   *
   * @param role 用户名
   * @return List<PermissionVO> 对象
   */
  @GetMapping("/findPermissionByRole/{role}")
  public List<PermissionVO> findPermissionByRole(@PathVariable String role) {
    return permissionService.findPermissionByRoleName(role);

  }

}
