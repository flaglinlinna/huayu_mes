package com.system.check.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import com.app.base.entity.BaseEntity;

@Entity
@DynamicUpdate
@Table(name="sys_flow_step")
public class WorkflowStep extends BaseEntity {
	private static final long serialVersionUID = -5994934265197482398L;
	public static final String TABLE_NAME = "sys_flow_step";

	/**
	 * 流程id (主表id)
	 */
	@Column
	protected Long bsFlowId;
	
	/**
	 *步骤名称
	 */
	@Column(length=100)
	protected String bsStepName;
	
	/**
	 * 审批步骤
	 */
	@Column
	protected int bsCheckGrade;
	
	/**
	 * 步骤审批人
	 */
	@Column(length=100)
	protected String bsCheckBy;
	
//	/**
//	 * 用户id
//	 */
//	@Column
//	protected Long bsCheckId;
//	/**
//	 * 用户名称
//	 */
//	@Column(length=100)
//	protected String bsCheckName;

	public Long getBsFlowId() {
		return bsFlowId;
	}

	public void setBsFlowId(Long bsFlowId) {
		this.bsFlowId = bsFlowId;
	}

	public String getBsStepName() {
		return bsStepName;
	}

	public void setBsStepName(String bsStepName) {
		this.bsStepName = bsStepName;
	}

	public int getBsCheckGrade() {
		return bsCheckGrade;
	}

	public void setBsCheckGrade(int bsCheckGrade) {
		this.bsCheckGrade = bsCheckGrade;
	}

	public String getBsCheckBy() {
		return bsCheckBy;
	}

	public void setBsCheckBy(String bsCheckBy) {
		this.bsCheckBy = bsCheckBy;
	}

//	public Long getBsCheckId() {
//		return bsCheckId;
//	}
//
//	public void setBsCheckId(Long bsCheckId) {
//		this.bsCheckId = bsCheckId;
//	}
//
//	public String getBsCheckName() {
//		return bsCheckName;
//	}
//
//	public void setBsCheckName(String bsCheckName) {
//		this.bsCheckName = bsCheckName;
//	}
	
	

}
