package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工作中心维护表
 *
 */
@Entity(name = "BjWorkCenter")
@Table(name = BjWorkCenter.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class BjWorkCenter extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "BJ_base_WORKCENTER";

	/**
	 * 大类-根据这大类下发到制造部的不同工作中心,必须选择大类，而且只有这几个大类
	 * 五金:hardware
	 * 注塑:molding
	 * 表面处理:surface
	 * 组装:packag
	 * 外协:out
	 */
	@ApiModelProperty(name = "bsCode", value = "大类")
	@Column(length = 50)
	protected String bsCode;
	
	/**
	 * 工作中心编号
	 */
	@ApiModelProperty(name = "workcenterCode", value = "工作中心编号")
	@Column(length = 50)
	protected String workcenterCode;

	/**
	 * 工作中心名称
	 */
	@ApiModelProperty(name = "workcenterName", value = "工作中心名称")
	@Column(length = 50)
	protected String workcenterName;

	/**
	 * 有效状态 （1：正常 / 0：禁用）
	 */
	@ApiModelProperty(name = "checkStatus", value = "有效状态（1：正常 / 0：禁用）")
	@Column
	protected Integer checkStatus = 1;

	public String getWorkcenterCode() {
		return workcenterCode;
	}

	public void setWorkcenterCode(String workcenterCode) {
		this.workcenterCode = workcenterCode;
	}

	public String getWorkcenterName() {
		return workcenterName;
	}

	public void setWorkcenterName(String workcenterName) {
		this.workcenterName = workcenterName;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getBsCode() {
		return bsCode;
	}

	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	
}
