package com.system.check.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity
@DynamicUpdate
@Table(name="sys_check_info")
public class CheckInfo extends BaseEntity {
	private static final long serialVersionUID = -5994934265197482391L;
	public static final String TABLE_NAME = "sys_check_info";

	/**
	 * 步骤审批人
	 */
	@Column(length=100)
	protected String bsCheckBy;

	/**
	 * 用户id
	 */
	@Column
	protected Long bsCheckId;
	/**
	 * 用户名称
	 */
	@Column(length=100)
	protected String bsCheckName;

	/**
	 * 业务数据ID
	 */
	@Column
	protected Long bsRecordId;

	/**
	 * 审批步骤id
	 */
	@Column
	protected Long bsStepId;


	/**
	 * 审批步骤名称
	 */
	@Column(length=100)
	protected String bsStepName;

	/**
	 *审批步骤层次
	 */
	@Column
	protected int bsCheckGrade;

	/**
	 * 流程步骤审批状态
	 * 1:同意
	 * 2：退回
	 * 3;驳回重写
	 */
	protected int bsStepCheckStatus;

	/**
	 * 审批意见
	 */
	@Column(length=100)
	protected String bsCheckComments;

	/**
	 * 审批备注
	 */
	@Column(length=1000)
	protected String bsCheckDes;

	/**
	 * 流程审批人
	 */
	@Column(length=100)
	protected String bsCheckPerson;

	/**
	 * 流程编码
	 */
	@Column(length=100)
	protected String bsCheckCode;

	/**
	 * 步骤审批人的单位组织
	 */
	@Column(length=500)
	protected String bsCheckOrg;



    public String getBsCheckBy() {
        return bsCheckBy;
    }

    public void setBsCheckBy(String bsCheckBy) {
        this.bsCheckBy = bsCheckBy;
    }

    public Long getBsCheckId() {
        return bsCheckId;
    }

    public void setBsCheckId(Long bsCheckId) {
        this.bsCheckId = bsCheckId;
    }

    public String getBsCheckName() {
        return bsCheckName;
    }

    public void setBsCheckName(String bsCheckName) {
        this.bsCheckName = bsCheckName;
    }

    public Long getBsRecordId() {
        return bsRecordId;
    }

    public void setBsRecordId(Long bsRecordId) {
        this.bsRecordId = bsRecordId;
    }

    public Long getBsStepId() {
        return bsStepId;
    }

    public void setBsStepId(Long bsStepId) {
        this.bsStepId = bsStepId;
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

    public int getBsStepCheckStatus() {
        return bsStepCheckStatus;
    }

    public void setBsStepCheckStatus(int bsStepCheckStatus) {
        this.bsStepCheckStatus = bsStepCheckStatus;
    }

    public String getBsCheckComments() {
        return bsCheckComments;
    }

    public void setBsCheckComments(String bsCheckComments) {
        this.bsCheckComments = bsCheckComments;
    }

    public String getBsCheckDes() {
        return bsCheckDes;
    }

    public void setBsCheckDes(String bsCheckDes) {
        this.bsCheckDes = bsCheckDes;
    }

    public String getBsCheckPerson() {
        return bsCheckPerson;
    }

    public void setBsCheckPerson(String bsCheckPerson) {
        this.bsCheckPerson = bsCheckPerson;
    }

    public String getBsCheckCode() {
        return bsCheckCode;
    }

    public void setBsCheckCode(String bsCheckCode) {
        this.bsCheckCode = bsCheckCode;
    }

	public String getBsCheckOrg() {
		return bsCheckOrg;
	}

	public void setBsCheckOrg(String bsCheckOrg) {
		this.bsCheckOrg = bsCheckOrg;
	}


}
