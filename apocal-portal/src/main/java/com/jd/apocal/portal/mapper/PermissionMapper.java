package com.jd.apocal.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.apocal.portal.model.entity.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 */
public interface PermissionMapper extends BaseMapper<Permission> {

  /**
   * 通过角色名查询菜单
   *
   * @param role 角色名称
   * @return 菜单列表
   */
  List findPermissionByRoleName(@Param("role") String role);
}