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
     * 在职人数
     */
    @ApiModelProperty(name = "empNum", value = "在职人数")
    @Column
    protected Integer empNum;
    

    /**
     * 负责人
     */
    @ApiModelProperty(name = "leadBy", value = "负责人")
    @Column(length = 30)
    protected String leadBy;
    
    /**
     * 负责人Id
     */
    @ApiModelProperty(name = "leadById", value = "负责人")
    protected Long leadById;


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

	public Integer getEmpNum() {
		return empNum;
	}

	public void setEmpNum(Integer empNum) {
		this.empNum = empNum;
	}
	

	public Long getLeadById() {
		return leadById;
	}

	public void setLeadById(Long leadById) {
		this.leadById = leadById;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("组织编号:").append(this.orgCode);
		sb.append(",组织名称:").append(this.orgName);
		sb.append(",父Id:").append(this.parentId);
//		sb.append(",flevel:").append(this.flevel);
//		sb.append(",zindex:").append(this.zindex);
		sb.append(",在职人数:").append(this.empNum);
		sb.append(",负责人:").append(this.leadBy);
		sb.append(",联系电话:").append(this.mobile);
		sb.append(",描述:").append(this.description);
//		sb.append(",menuType:").append(this.menuType);
		sb.append(";");
		return sb.toString();
	}
	
}
