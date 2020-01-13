package com.jd.apocal.portal.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 角色权限表
 * </p>
 */
@Data
@TableName("role_permission")
public class RolePermission extends Model<RolePermission> {

  private static final long serialVersionUID = 1L;

  /**
   * ID
   */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 角色ID
   */
  @TableId(type = IdType.INPUT)
  private Integer roleId;
  /**
   * 权限ID
   */
  @TableId(type = IdType.INPUT)
  private Integer permissionId;

  @Override
  protected Serializable pkVal() {
    return this.roleId;
  }

  @Override
  public String toString() {
    return "RolePermission{" +
        ", roleId=" + roleId +
        ", permissionId=" + permissionId +
        "}";
  }
}
