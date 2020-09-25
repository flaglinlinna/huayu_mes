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
    public static final String TABLE_NAME = "SYS_ORGANIZATION";

    /**
     * 组织编号
     */
    @ApiModelProperty(name = "orgCode", value = "组织编号")
    @Column(length = 50)
    protected String orgCode;
    
    /**
     * 组织名称
     */
    @ApiModelProperty(name = "orgName", value = "组织名称")
    @Column(length = 50)
    protected String orgName;
    
    /**
     * 父菜单id
     */
    @ApiModelProperty(name = "parentId", value = "父菜单id")
    @Column
    protected Long parentId;
    
    /**
     * 组织级别
     */
    @ApiModelProperty(name = "flevel", value = "组织级别")
    @Column(length = 30)
    protected String flevel;

    

    /**
     * 组织排序
     */
    @ApiModelProperty(name = "zindex", value = "菜单排序")
    @Column
    protected Integer zindex;
    

    /**
     * 负责人
     */
    @ApiModelProperty(name = "leadBy", value = "负责人")
    @Column(length = 30)
    protected String leadBy;


    /**
     * 联系电话
     */
    @ApiModelProperty(name = "mobile", value = "联系电话")
    @Column(length = 50)
    protected String mobile;


    /**
     * 描述
     */
    @ApiModelProperty(name = "description", value = "描述")
    @Column(length = 50)
    protected String description;
    
    /**
     * 组织分类（）
     * 暂时保留字段
     */
    @ApiModelProperty(name = "menuType", value = "组织分类（）")
    @Column
    protected Integer menuType;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public String getLeadBy() {
		return leadBy;
	}

	public void setLeadBy(String leadBy) {
		this.leadBy = leadBy;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public String getFlevel() {
		return flevel;
	}

	public void setFlevel(String flevel) {
		this.flevel = flevel;
	}
	
}
