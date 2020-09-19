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
	public static final String TABLE_NAME = "basic_client_process";

	/**
	 * 关联客户表
	 */
	@ApiModelProperty(name = "pkClient", value = "客户ID")
	@Column
	protected Long pkClient;

	@ApiModelProperty(name = "client", hidden = true, value = "客户ID")
	@ManyToOne
	@JoinColumn(name = "pkClient", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected Client client;

	/**
	 * 关联工序表
	 */
	@ApiModelProperty(name = "pkProcess", value = "工序ID")
	@Column
	protected Long pkProcess;

	@ApiModelProperty(name = "process", hidden = true, value = "工序ID")
	@ManyToOne
	@JoinColumn(name = "pkProcess", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected Process process;

	/**
	 * 工序顺序
	 */
	@ApiModelProperty(name = "bsOrder", value = "工序顺序")
	@Column(length = 10)
	protected String bsOrder;	
	
	/**
     * 过程属性（0：被勾选 / 1：无） 默认被勾选
     */
    @ApiModelProperty(name = "bsProcAttrib", value = "过程属性（0：被勾选 / 1：无）")
    @Column
    protected Integer bsProcAttrib = 0;

	public Long getPkClient() {
		return pkClient;
	}

	public void setPkClient(Long pkClient) {
		this.pkClient = pkClient;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Long getPkProcess() {
		return pkProcess;
	}

	public void setPkProcess(Long pkProcess) {
		this.pkProcess = pkProcess;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public String getBsOrder() {
		return bsOrder;
	}

	public void setBsOrder(String bsOrder) {
		this.bsOrder = bsOrder;
	}

	public Integer getBsProcAttrib() {
		return bsProcAttrib;
	}

	public void setBsProcAttrib(Integer bsProcAttrib) {
		this.bsProcAttrib = bsProcAttrib;
	}
    
}
