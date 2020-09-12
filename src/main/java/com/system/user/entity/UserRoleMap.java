package com.system.user.entity;

import com.app.base.entity.BaseEntity;
import com.system.role.entity.PrimaryKey;
import com.system.role.entity.RolePermissionMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户角色关系表
 */
@Entity(name = "UserRoleMap")
@Table(name = UserRoleMap.TABLE_NAME)
//@IdClass(PrimaryKeyUser.class)
@DynamicUpdate
@ApiModel
public class UserRoleMap extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "sys_user_role";

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "userId", value = "用户ID")
    @Column(name = "user_id")
    @NotNull
    protected Long userId;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleId", value = "角色ID")
    @Column(name = "role_id")
    @NotNull
    protected Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
