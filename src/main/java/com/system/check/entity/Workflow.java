package com.system.check.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import com.app.base.entity.BaseEntity;


@Entity
@DynamicUpdate
@Table(name="sys_workflow")
public class Workflow extends BaseEntity {
	private static final long serialVersionUID = -5994934265197482390L;
	public static final String TABLE_NAME = "sys_workflow";
	/**
	 * 流程编码
	 */
	@Column(length=100)
	protected String bsFlowCode;
	/**
	 * 流程名称
	 */
	@Column(length=100)
	protected String bsFlowName;
	
	/**
	 *流程描述 
	 */
	@Column(length=100)
	protected String bsFlowDescribe;
	
	/**
	 * 流程类别
	 */
	@Column(length=100)
	protected String bsFlowType;
	
	/**
	 * 流程状态
	 */
	@Column(length=100)
	protected String bsFlowStatus;

	public String getBsFlowName() {
		return bsFlowName;
	}

	public void setBsFlowName(String bsFlowName) {
		this.bsFlowName = bsFlowName;
	}

	public String getBsFlowDescribe() {
		return bsFlowDescribe;
	}

	public void setBsFlowDescribe(String bsFlowDescribe) {
		this.bsFlowDescribe = bsFlowDescribe;
	}

	public String getBsFlowType() {
		return bsFlowType;
	}

	public void setBsFlowType(String bsFlowType) {
		this.bsFlowType = bsFlowType;
	}

	public String getBsFlowStatus() {
		return bsFlowStatus;
	}

	public void setBsFlowStatus(String bsFlowStatus) {
		this.bsFlowStatus = bsFlowStatus;
	}

	public String getBsFlowCode() {
		return bsFlowCode;
	}

	public void setBsFlowCode(String bsFlowCode) {
		this.bsFlowCode = bsFlowCode;
	}
	
	

}
