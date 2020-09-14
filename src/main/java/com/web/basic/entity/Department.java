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
	    @ApiModelProperty(name = "deCode", value = "部门编码")
	    @Column(length = 10)
	    protected String deCode;

	    /**
	     * 部门名称
	     */
	    @ApiModelProperty(name = "deName", value = "部门名称")
	    @Column(length = 15)
	    protected String deName;
	    
	    /**
	     *  部门经理
	     */
	    @ApiModelProperty(name = "deManager", value = "部门经理")
	    @Column(length = 20)
	    protected String deManager;
	    
	    /**
	     *  部门经理联系方式
	     */
	    @ApiModelProperty(name = "deManagerTel", value = "部门经理电话")
	    @Column(length = 20)
	    protected String deManagerTel;
	    
	    /**
	     * 状态（0：正常 / 1：禁用）
	     */
	    @ApiModelProperty(name = "deStatus", value = "状态（0：正常 / 1：禁用）")
	    @Column
	    protected Integer deStatus = 0;

		public String getDeCode() {
			return deCode;
		}

		public void setDeCode(String deCode) {
			this.deCode = deCode;
		}

		public String getDeName() {
			return deName;
		}

		public void setDeName(String deName) {
			this.deName = deName;
		}

		public String getDeManager() {
			return deManager;
		}

		public void setDeManager(String deManager) {
			this.deManager = deManager;
		}

		public String getDeManagerTel() {
			return deManagerTel;
		}

		public void setDeManagerTel(String deManagerTel) {
			this.deManagerTel = deManagerTel;
		}

		public Integer getDeStatus() {
			return deStatus;
		}

		public void setDeStatus(Integer deStatus) {
			this.deStatus = deStatus;
		}
	    
	    
}
