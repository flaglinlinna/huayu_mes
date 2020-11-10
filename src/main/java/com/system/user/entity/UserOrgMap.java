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
 * 用户组织结构关系表
 */
@Entity(name = "UserOrgMap")
@Table(name = UserOrgMap.TABLE_NAME)
//@IdClass(PrimaryKeyUser.class)
@DynamicUpdate
@ApiModel
public class UserOrgMap extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "SYS_USER_ORG";

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "userId", value = "用户ID")
    @Column(name = "user_id")
    @NotNull
    protected Long userId;

    /**
     * 组织架构记录ID
     */
    @ApiModelProperty(name = "orgId", value = "组织架构记录ID")
    @Column(name = "org_id")
    @NotNull
    protected Long orgId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
