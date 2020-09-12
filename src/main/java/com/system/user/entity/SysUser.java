package com.system.user.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户基础信息表
 *
 */
@Entity(name = "SysUser")
@Table(name = SysUser.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "sys_user";

    /**
     * 用户账号
     */
    @ApiModelProperty(name = "bsCode", value = "用户账号")
    @Column(length = 50)
    protected String bsCode;

    /**
     * 用户名称
     */
    @ApiModelProperty(name = "bsName", value = "用户名称")
    @Column(length = 50)
    protected String bsName;

    /**
     * 密码
     */
    @ApiModelProperty(name = "bsPassword", value = "密码")
    @Column(length = 50)
    protected String bsPassword;

    /**
     * 真实名称
     */
    @ApiModelProperty(name = "realName", value = "真实名称")
    @Column(length = 100)
    protected String realName;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "mobile", value = "手机号")
    @Column(length = 50)
    protected String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email", value = "邮箱")
    @Column(length = 100)
    protected String email;

    /**
     * 性别（男/女）
     */
    @ApiModelProperty(name = "sex", value = "性别")
    @Column(length = 10)
    protected String sex;

    /**
     * 状态（0：正常 / 1：禁用）
     */
    @ApiModelProperty(name = "bsStatus", value = "状态（0：正常 / 1：禁用）")
    @Column
    protected Integer bsStatus = 0;

    /**
     * 注册来源
     */
    @ApiModelProperty(name = "registerSource", value = "注册来源")
    @Column(length = 50)
    protected String registerSource;

    /**
     * 角色ID
     */
    @Transient
    protected String roleIds;

    public String getBsCode() {
        return bsCode;
    }

    public void setBsCode(String bsCode) {
        this.bsCode = bsCode;
    }

    public String getBsName() {
        return bsName;
    }

    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public String getBsPassword() {
        return bsPassword;
    }

    public void setBsPassword(String bsPassword) {
        this.bsPassword = bsPassword;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getBsStatus() {
        return bsStatus;
    }

    public void setBsStatus(Integer bsStatus) {
        this.bsStatus = bsStatus;
    }

    public String getRegisterSource() {
        return registerSource;
    }

    public void setRegisterSource(String registerSource) {
        this.registerSource = registerSource;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
}
