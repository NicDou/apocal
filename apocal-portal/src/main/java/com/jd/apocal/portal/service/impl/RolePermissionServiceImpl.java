package com.jd.apocal.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.portal.mapper.RolePermissionMapper;
import com.jd.apocal.portal.model.entity.RolePermission;
import com.jd.apocal.portal.service.RolePermissionService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 */
@Service
public class RolePermissionServiceImpl extends
    ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

  @Override
  public void authPermissions(String roleId, String[] permissionIds) {
    /**
     * 删除原有权限
     */
    this.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));
    /**
     * 重新授权
     */
    if (ArrayUtils.isNotEmpty(permissionIds)) {
      for (String permissionId : permissionIds) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(new Integer(roleId));
        rolePermission.setPermissionId(new Integer(permissionId));
        this.save(rolePermission);
      }
    }
  }
}
