package com.system.role.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色菜单关系表
 */
@Entity(name = "RolePermissionMap")
@Table(name = RolePermissionMap.TABLE_NAME)
//@IdClass(PrimaryKey.class)
@DynamicUpdate
@ApiModel
public class RolePermissionMap extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "sys_role_permission";

    /**
     * 菜单ID
     */
    @ApiModelProperty(name = "permitId", value = "菜单ID")
    @Column(name = "permit_id")
    @NotNull
    protected Long permitId;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleId", value = "角色ID")
    @Column(name = "role_id")
    @NotNull
    protected Long roleId;

    public Long getPermitId() {
        return permitId;
    }

    public void setPermitId(Long permitId) {
        this.permitId = permitId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
