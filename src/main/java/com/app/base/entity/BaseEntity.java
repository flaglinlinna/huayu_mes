package com.app.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity extends IdEntity {
	private static final long serialVersionUID = -5249737644031588435L;

    /**
     * 是否删除(删除标识）
     * 0：否 1：是
     */
	@Column
	@NotNull
	@ApiModelProperty(name="delFlag",value="删除标志(0:未删除,1:已删除)")
    protected int delFlag = 0;
	
	/**
     * 删除人
     */
	@ApiModelProperty(name = "delBy", value = "删除人")
    @Column(length = 30)
    protected Long delBy;
	
	/**
     * 删除时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="delTime",value="删除时间")
	protected Date delTime;
	
	/**
     * 公司代码
     */
	@ApiModelProperty(name = "company", value = "公司代码")
    @Column(length = 30)
    protected String company;
	
	/**
     * 工厂编码
     */
	@ApiModelProperty(name = "factory", value = "工厂编码")
    @Column(length = 30)
    protected String factory;
	
	/**
     * 事业部编码
     */
	@ApiModelProperty(name = "division", value = "事业部编码")
    @Column(length = 30)
    protected String division;
	
	/**
     * 创建人
     */
    @Column
    @ApiModelProperty(name="createBy",value="创建人")
    protected Long createBy;
	
    /**
     * 创建时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="createDate",value="创建时间")
	protected Date createDate;

    /**
     * 上次修改人
     */
    @Column
    @ApiModelProperty(name="lastupdateBy",value="上次修改人")
    protected Long lastupdateBy;
	
    /**
     * 上次修改时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="lastupdateDate",value="上次修改时间")
	protected Date lastupdateDate;
	
	/**
     * 备注
     */
	@ApiModelProperty(name = "fmemo", value = "备注")
    @Column(length = 200)
    protected String fmemo;

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	

	public Long getDelBy() {
		return delBy;
	}

	public void setDelBy(Long delBy) {
		this.delBy = delBy;
	}

	public Date getDelTime() {
		return delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getLastupdateBy() {
		return lastupdateBy;
	}

	public void setLastupdateBy(Long lastupdateBy) {
		this.lastupdateBy = lastupdateBy;
	}

	public Date getLastupdateDate() {
		return lastupdateDate;
	}

	public void setLastupdateDate(Date lastupdateDate) {
		this.lastupdateDate = lastupdateDate;
	}

	public String getFmemo() {
		return fmemo;
	}

	public void setFmemo(String fmemo) {
		this.fmemo = fmemo;
	}

	
   
}
