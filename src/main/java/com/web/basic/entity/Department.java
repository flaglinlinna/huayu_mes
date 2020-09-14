package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 部门基础信息表
 *
 */
@Entity(name = "Department")
@Table(name = Department.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Department extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "basic_department";
	    
	    /**
	     * 部门编码
	     */
	    @ApiModelProperty(name = "bsCode", value = "部门编码")
	    @Column(length = 10)
	    protected String bsCode;

	    /**
	     * 部门名称
	     */
	    @ApiModelProperty(name = "bsName", value = "部门名称")
	    @Column(length = 15)
	    protected String bsName;
	    
	    /**
	     *  部门经理
	     */
	    @ApiModelProperty(name = "bsManager", value = "部门经理")
	    @Column(length = 20)
	    protected String bsManager;
	    
	    /**
	     *  部门经理联系方式
	     */
	    @ApiModelProperty(name = "bsManagerTel", value = "部门经理电话")
	    @Column(length = 20)
	    protected String bsManagerTel;
	    
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

		public String getBsManager() {
			return bsManager;
		}

		public void setBsManager(String bsManager) {
			this.bsManager = bsManager;
		}

		public String getBsManagerTel() {
			return bsManagerTel;
		}

		public void setBsManagerTel(String bsManagerTel) {
			this.bsManagerTel = bsManagerTel;
		}

		public Integer getBsStatus() {
			return bsStatus;
		}

		public void setBsStatus(Integer bsStatus) {
			this.bsStatus = bsStatus;
		}    
}
