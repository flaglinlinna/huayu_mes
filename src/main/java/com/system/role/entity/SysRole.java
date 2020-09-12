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
    public static final String TABLE_NAME = "sys_role";

    /**
     * 角色编号
     */
    @ApiModelProperty(name = "bsCode", value = "角色编号")
    @Column(length = 20)
    protected String bsCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(name = "bsName", value = "角色名称")
    @Column(length = 30)
    protected String bsName;

    /**
     * 角色描述
     */
    @ApiModelProperty(name = "descpt", value = "角色描述")
    @Column(length = 50)
    protected String descpt;

    /**
     * 状态（0：正常 / 1：禁用）
     */
    @ApiModelProperty(name = "bsStatus", value = "状态（0：正常 / 1：禁用）")
    @Column
    protected Integer bsStatus = 0;

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

    public String getDescpt() {
        return descpt;
    }

    public void setDescpt(String descpt) {
        this.descpt = descpt;
    }

    public Integer getBsStatus() {
        return bsStatus;
    }

    public void setBsStatus(Integer bsStatus) {
        this.bsStatus = bsStatus;
    }
}
