package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import com.web.basic.entity.Defective;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 工序基础信息表
 *
 */
@Entity(name = "Proc")
@Table(name = Proc.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Proc extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "BJ_base_PROC";

	/**
	 * 工作中心id
	 */
	@ApiModelProperty(name = "bjWorkCenter", value = "工作中心id")
	@Column(length = 20)
	protected Long workcenterId;
	
	@ApiModelProperty(name = "workcenter", hidden = true, value = "工作中心ID")
	@ManyToOne
	@JoinColumn(name = "workcenterId", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected BjWorkCenter bjWorkCenter;

	/**
	 * 工序编号
	 */
	@ApiModelProperty(name = "procNo", value = "工序编号")
	@Column(length = 50)
	protected String procNo;

	/**
	 * 工序名称
	 */
	@ApiModelProperty(name = "procName", value = "工序名称")
	@Column(length = 50)
	protected String procName;

	/**
	 * 工序顺序号
	 */
	@ApiModelProperty(name = "procOrder", value = "工序顺序号")
	@Column(length = 50)
	protected String procOrder;

	/**
	 * 审核标识
	 */
	@ApiModelProperty(name = "checkStatus", value = "审核标识")
	@Column(length = 50)
	protected Integer checkStatus = 1;

	public String getProcNo() {
		return procNo;
	}

	public void setProcNo(String procNo) {
		this.procNo = procNo;
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public String getProcOrder() {
		return procOrder;
	}

	public void setProcOrder(String procOrder) {
		this.procOrder = procOrder;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Long getWorkcenterId() {
		return workcenterId;
	}

	public void setWorkcenterId(Long workcenterId) {
		this.workcenterId = workcenterId;
	}

	public BjWorkCenter getBjWorkCenter() {
		return bjWorkCenter;
	}

	public void setBjWorkCenter(BjWorkCenter bjWorkCenter) {
		this.bjWorkCenter = bjWorkCenter;
	}
	
}
