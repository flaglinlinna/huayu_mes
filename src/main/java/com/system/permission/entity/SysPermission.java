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
    public static final String TABLE_NAME = "SYS_MEMU";


    /**
     * 菜单编号
     */
    @ApiModelProperty(name = "menuCode", value = "菜单编号")
    @Column(length = 50)
    protected String menuCode;

    /**
     * 菜单名称
     */
    @ApiModelProperty(name = "menuName", value = "菜单名称")
    @Column(length = 50)
    protected String menuName;
    
    /**
     * 菜单图标名称
     */
    @ApiModelProperty(name = "menuIcon", value = "菜单图标名称")
    @Column(length = 50)
    protected String menuIcon;
    
    /**
     * 菜单url
     */
    @ApiModelProperty(name = "pageUrl", value = "菜单url")
    @Column(length = 50)
    protected String pageUrl;

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
     * 权限分类（0 菜单；1 按钮）
     */
    @ApiModelProperty(name = "istype", value = "权限分类（0 菜单；1 功能）")
    @Column
    protected Integer istype;

    /**
     * 描述
     */
    @ApiModelProperty(name = "description", value = "描述")
    @Column(length = 100)
    protected String description;

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("菜单编号:").append(this.menuCode);
		sb.append(",菜单名称:").append(this.menuName);
		sb.append(",菜单图标:").append(this.menuIcon);
		sb.append(",菜单url:").append(this.pageUrl);
		sb.append(",父ID:").append(this.parentId);
		sb.append(",zindex:").append(this.zindex);
		sb.append(",类别:").append(this.istype==0?"菜单":"按钮");
		sb.append(",描述:").append(this.description);
		sb.append(";");
		return sb.toString();
	}
}
