package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity(name = "ProdProcDetail")
@Table(name = ProdProcDetail.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProdProcDetail extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "basic_prod_proc_detail";

	/**
	 * 产品工艺流程主表ID
	 */
	@ApiModelProperty(name = "bsProcId", value = "产品工艺流程主表ID")
	@Column
	protected Long bsProcId;

	/**
	 * 关联工序表的ID
	 */
	@ApiModelProperty(name = "bsWoProcId", value = "工序信息ID")
	@Column
	protected Long bsWoProcId;

	@ApiModelProperty(name = "woProc", hidden = true, value = "工序信息")
	@ManyToOne
	@JoinColumn(name = "bsWoProcId", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected WoProc woProc;

	/**
	 * 工序排列序号
	 */
	@ApiModelProperty(name = "bsSort", value = "工序排列序号")
	@Column
	protected Integer bsSort;

	public Long getBsProcId() {
		return bsProcId;
	}

	public void setBsProcId(Long bsProcId) {
		this.bsProcId = bsProcId;
	}

	public Long getBsWoProcId() {
		return bsWoProcId;
	}

	public void setBsWoProcId(Long bsWoProcId) {
		this.bsWoProcId = bsWoProcId;
	}

	public Integer getBsSort() {
		return bsSort;
	}

	public void setBsSort(Integer bsSort) {
		this.bsSort = bsSort;
	}
	
	
}
