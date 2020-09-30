package com.web.basic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 员工基础信息表
 *
 */
@Entity(name = "Employee")
@Table(name = Employee.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Employee extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "MES_BASE_EMPLOYEE";
	    
	    /**
	     * 员工工号
	     */
	    @ApiModelProperty(name = "empCode", value = "员工工号")
	    @Column(length = 50)
	    protected String empCode;

	    /**
	     * 员工姓名
	     */
	    @ApiModelProperty(name = "empName", value = "员工姓名")
	    @Column(length = 50)
	    protected String empName;
	    
	    /**
	     * 身份证
	     */
	    @ApiModelProperty(name = "empIdNo", value = "身份证")
	    @Column(length = 50)
	    protected String empIdNo;

	    /**
	     * 入职日期
	     */	        
	    @Column
	    @Temporal(TemporalType.TIMESTAMP)
	    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
		@ApiModelProperty(name="joinDate",value="入职日期")
		protected Date joinDate;
	   
	    /**
	     * 离职日期
	     */
	    
	    @Column
	    @Temporal(TemporalType.TIMESTAMP)
	    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
		@ApiModelProperty(name="leaveDate",value="离职日期")
		protected Date leaveDate;
 
	    /**
	     * 员工类型
	     */
	    @ApiModelProperty(name = "empType", value = "员工类型")
	    @Column(length = 50)
	    protected String empType;

	    /**
	     * 部门ID  [无部门表，则不建立外键]
	     */	    
	    
	    @ApiModelProperty(name="deptId",value="部门ID")
	     @Column
	     protected Long deptId;

	    /**
	     * 员工状态1:在职 0:离职
	     */
	    @ApiModelProperty(name = "empStatus", value = "状态")
	    @Column(length = 1)
	    protected Integer empStatus = 1;

		public String getEmpCode() {
			return empCode;
		}

		public void setEmpCode(String empCode) {
			this.empCode = empCode;
		}

		public String getEmpName() {
			return empName;
		}

		public void setEmpName(String empName) {
			this.empName = empName;
		}

		public String getEmpIdNo() {
			return empIdNo;
		}

		public void setEmpIdNo(String empIdNo) {
			this.empIdNo = empIdNo;
		}

		public Date getJoinDate() {
			return joinDate;
		}

		public void setJoinDate(Date joinDate) {
			this.joinDate = joinDate;
		}

		public Date getLeaveDate() {
			return leaveDate;
		}

		public void setLeaveDate(Date leaveDate) {
			this.leaveDate = leaveDate;
		}

		public String getEmpType() {
			return empType;
		}

		public void setEmpType(String empType) {
			this.empType = empType;
		}

		public Long getDeptId() {
			return deptId;
		}

		public void setDeptId(Long deptId) {
			this.deptId = deptId;
		}

		public Integer getEmpStatus() {
			return empStatus;
		}

		public void setEmpStatus(Integer empStatus) {
			this.empStatus = empStatus;
		}

}