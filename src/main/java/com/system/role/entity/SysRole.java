package com.system.role.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 角色基础信息表
 *
 */
@Entity(name = "SysRole")
@Table(name = SysRole.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "SYS_ROLE";

    /**
     * 角色编号
     */
    @ApiModelProperty(name = "roleCode", value = "角色编号")
    @Column(length = 50)
    protected String roleCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(name = "roleName", value = "角色名称")
    @Column(length = 50)
    protected String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty(name = "description", value = "角色描述")
    @Column(length = 100)
    protected String description;

    /**
     * 状态（0：正常 / 1：禁用）
     */
    @ApiModelProperty(name = "status", value = "状态（0：正常 / 1：禁用）")
    @Column
    protected Integer status = 0;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("角色编号:").append(this.roleCode);
		sb.append(",角色名称:").append(this.roleName);
		sb.append(",角色描述:").append(this.description);
		sb.append(",状态:").append(this.status==0?"正常":"禁用");
		sb.append(";");
		return sb.toString();
	}
}
