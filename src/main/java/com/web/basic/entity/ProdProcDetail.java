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
	public static final String TABLE_NAME = "MES_BASE_ITEM_PROC";

	/**
	 * 关联物料ID
	 */
	@ApiModelProperty(name = "itemId", value = "物料ID")
	@Column
	protected Long itemId;

	@ApiModelProperty(name = "mtrial", hidden = true, value = "工序信息")
	@ManyToOne
	@JoinColumn(name = "itemId", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected Mtrial mtrial;
	
	/**
     * 物料编码
     */
    @ApiModelProperty(name = "itemNo", value = "物料编码")
    @Column(length = 50)
    protected String itemNo;
    
    /**
	 * 关联工序ID
	 */
	@ApiModelProperty(name = "procId", value = "工序ID")
	@Column
	protected Long procId;

	@ApiModelProperty(name = "process", hidden = true, value = "工序信息")
	@ManyToOne
	@JoinColumn(name = "procId", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected Mtrial Process;

	/**
	 * 工序排列序号
	 */
	@ApiModelProperty(name = "procOrder", value = "工序排列序号")
	@Column
	protected Integer procOrder;

	/**
	 * 过程属性
	 */
	@ApiModelProperty(name = "jobAttr", value = "过程属性")
	@Column
	protected Integer jobAttr=0;
	
	/**
	 * 报工标示
	 */
	@ApiModelProperty(name = "flag", value = "报工标示")
	@Column
	protected Integer flag=0;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Mtrial getMtrial() {
		return mtrial;
	}

	public void setMtrial(Mtrial mtrial) {
		this.mtrial = mtrial;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Long getProcId() {
		return procId;
	}

	public void setProcId(Long procId) {
		this.procId = procId;
	}

	public Mtrial getProcess() {
		return Process;
	}

	public void setProcess(Mtrial process) {
		Process = process;
	}

	public Integer getProcOrder() {
		return procOrder;
	}

	public void setProcOrder(Integer procOrder) {
		this.procOrder = procOrder;
	}

	public Integer getJobAttr() {
		return jobAttr;
	}

	public void setJobAttr(Integer jobAttr) {
		this.jobAttr = jobAttr;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	
	
}
