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

/**
 * 客户通用工艺基础信息表
 *
 */
@Entity(name = "ClientProcessMap")
@Table(name = ClientProcessMap.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ClientProcessMap extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "MES_BASE_CUST_PROC";

	/**
	 * 关联客户表
	 */
	@ApiModelProperty(name = "custId", value = "客户ID")
	@Column
	protected Long custId;

	@ApiModelProperty(name = "client", hidden = true, value = "客户ID")
	@ManyToOne
	@JoinColumn(name = "custId", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected Client client;

	/**
	 * 关联工序表
	 */
	@ApiModelProperty(name = "procId", value = "工序ID")
	@Column
	protected Long procId;

	@ApiModelProperty(name = "process", hidden = true, value = "工序ID")
	@ManyToOne
	@JoinColumn(name = "procId", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected Process process;

	/**
	 * 工序顺序
	 */
	@ApiModelProperty(name = "procOrder", value = "工序顺序")
	@Column(length = 10)
	protected String procOrder;	
	
	/**
     * 过程属性（0：被勾选 / 1：无） 默认被勾选
     */
    @ApiModelProperty(name = "jobAttr", value = "过程属性（0：被勾选 / 1：无）")
    @Column
    protected Integer jobAttr = 0;
    
    /**
     * 报工标示
     */
    @ApiModelProperty(name = "flag", value = "报工标示")
    @Column
    protected Integer flag = 0;

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Long getProcId() {
		return procId;
	}

	public void setProcId(Long procId) {
		this.procId = procId;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public String getProcOrder() {
		return procOrder;
	}

	public void setProcOrder(String procOrder) {
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


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("客户ID:").append(this.custId);
		sb.append(",工序ID:").append(this.procId);
		sb.append(",工序顺序:").append(this.procOrder);
		sb.append(",过程属性:").append(this.jobAttr==0?"是":"否");
		sb.append(",报工标示:").append(this.flag);
		return sb.toString();
	}
}
