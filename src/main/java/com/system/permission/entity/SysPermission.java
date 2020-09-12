package com.system.permission.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 菜单基础信息表
 *
 */
@Entity(name = "SysPermission")
@Table(name = SysPermission.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysPermission extends BaseEntity{
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "sys_permission";


    /**
     * 菜单名称
     */
    @ApiModelProperty(name = "bsName", value = "菜单名称")
    @Column(length = 30)
    protected String bsName;

    /**
     * 父菜单id
     */
    @ApiModelProperty(name = "parentId", value = "父菜单id")
    @Column
    protected Long parentId;

    /**
     * 菜单排序
     */
    @ApiModelProperty(name = "zindex", value = "菜单排序")
    @Column
    protected Integer zindex;

    /**
     * 权限分类（0 菜单；1 功能）
     */
    @ApiModelProperty(name = "istype", value = "权限分类（0 菜单；1 功能）")
    @Column
    protected Integer istype;

    /**
     * 描述
     */
    @ApiModelProperty(name = "descpt", value = "描述")
    @Column(length = 50)
    protected String descpt;

    /**
     * 菜单编号
     */
    @ApiModelProperty(name = "bsCode", value = "菜单编号")
    @Column(length = 20)
    protected String bsCode;

    /**
     * 菜单图标名称
     */
    @ApiModelProperty(name = "bsIcon", value = "菜单图标名称")
    @Column(length = 30)
    protected String bsIcon;

    /**
     * 菜单url
     */
    @ApiModelProperty(name = "pageUrl", value = "菜单url")
    @Column(length = 50)
    protected String pageUrl;

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

	public Integer getZindex() {
		return zindex;
	}

	public void setZindex(Integer zindex) {
		this.zindex = zindex;
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

	public String getBsIcon() {
		return bsIcon;
	}

	public void setBsIcon(String bsIcon) {
		this.bsIcon = bsIcon;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

    
}
