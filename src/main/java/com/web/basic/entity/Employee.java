package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 客户基础信息表
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
	     * 客户编码
	     */
	    @ApiModelProperty(name = "custNo", value = "客户编码")
	    @Column(length = 50)
	    //@Column(columnDefinition = "varchar(50) comment '客户编码'")
	    protected String custNo;

	    /**
	     * 客户全称
	     */
	    @ApiModelProperty(name = "custName", value = "客户全称")
	    @Column(length = 50)
	    protected String custName;
	    
	    /**
	     * 状态1:正常 0:禁用
	     */
	    @ApiModelProperty(name = "checkStatus", value = "状态")
	    @Column(length = 1)
	    protected Integer checkStatus = 1;

		   
	    
}
