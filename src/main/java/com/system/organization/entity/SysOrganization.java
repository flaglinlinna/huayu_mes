package com.system.organization.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 组织基础信息表
 *
 */
@Entity(name = "SysOrganization")
@Table(name = SysOrganization.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysOrganization extends BaseEntity{
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "sys_organization";


    /**
     * 组织名称
     */
    @ApiModelProperty(name = "bsName", value = "组织名称")
    @Column(length = 30)
    protected String bsName;
    
    /**
     * 组织级别
     */
    @ApiModelProperty(name = "bsLevel", value = "组织级别")
    @Column(length = 30)
    protected String bsLevel;

    /**
     * 父菜单id
     */
    @ApiModelProperty(name = "parentId", value = "父菜单id")
    @Column
    protected Long parentId;

    /**
     * 组织排序
     */
    @ApiModelProperty(name = "bsZindex", value = "菜单排序")
    @Column
    protected Integer bsZindex;

    /**
     * 组织分类（）
     * 暂时保留字段
     */
    @ApiModelProperty(name = "istype", value = "组织分类（）")
    @Column
    protected Integer istype;

    /**
     * 描述
     */
    @ApiModelProperty(name = "descpt", value = "描述")
    @Column(length = 50)
    protected String descpt;

    /**
     * 组织编号
     */
    @ApiModelProperty(name = "bsCode", value = "组织编号")
    @Column(length = 20)
    protected String bsCode;

    /**
     * 负责人
     */
    @ApiModelProperty(name = "bsPrincipal", value = "负责人")
    @Column(length = 30)
    protected String bsPrincipal;

    /**
     * 联系电话
     */
    @ApiModelProperty(name = "bsMobile", value = "联系电话")
    @Column(length = 50)
    protected String bsMobile;

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getIstype() {
		return istype;
	}

	public void setIstype(Integer istype) {
		this.istype = istype;
	}

	public String getDescpt() {
		return descpt;
	}

	public void setDescpt(String descpt) {
		this.descpt = descpt;
	}

	public String getBsCode() {
		return bsCode;
	}

	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}

	public Integer getBsZindex() {
		return bsZindex;
	}

	public void setBsZindex(Integer bsZindex) {
		this.bsZindex = bsZindex;
	}

	public String getBsPrincipal() {
		return bsPrincipal;
	}

	public void setBsPrincipal(String bsPrincipal) {
		this.bsPrincipal = bsPrincipal;
	}

	public String getBsMobile() {
		return bsMobile;
	}

	public void setBsMobile(String bsMobile) {
		this.bsMobile = bsMobile;
	}

	public String getBsLevel() {
		return bsLevel;
	}

	public void setBsLevel(String bsLevel) {
		this.bsLevel = bsLevel;
	}
	

    
}
