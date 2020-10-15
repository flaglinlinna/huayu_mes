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
 * 客户基础信息表
 *
 */
@Entity(name = "BarcodeRule")
@Table(name = BarcodeRule.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class BarcodeRule extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "mes_base_barcode_s_rule";

	/**
	 * 客户ID
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
	 * 物料ID
	 */
	@ApiModelProperty(name = "itemId", value = "物料ID")
	@Column
	protected Long itemId;

	@ApiModelProperty(name = "mtrial", hidden = true, value = "物料ID")
	@ManyToOne
	@JoinColumn(name = "itemId", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected Mtrial mtrial;
	
	/**
	 * 物料编码【存物料ID带出来的物料编码】
	 */
	@ApiModelProperty(name = "itemNo", value = "物料编码")
	@Column(length = 100)
	protected String itemNo;

	/**
	 * 客户物料编码
	 */
	@ApiModelProperty(name = "itemNoCus", value = "客户物料编码")
	@Column(length = 100)
	protected String itemNoCus;
	
	/**
	 * 华勤物料编码
	 */
	@ApiModelProperty(name = "itemNoInside", value = "华勤物料编码")
	@Column(length = 100)
	protected String itemNoInside;

	/**
	 * 开始位置
	 */
	@ApiModelProperty(name = "positionBegin", value = "开始位置")
	@Column
	protected Long positionBegin;
	
	/**
	 * 结束位置
	 */
	@ApiModelProperty(name = "positionEnd", value = "结束位置")
	@Column
	protected Long positionEnd;
	
	/**
	 * 验证数据
	 */
	@ApiModelProperty(name = "chkString", value = "验证数据")
	@Column(length = 100)
	protected String chkString;
	
	/**
	 * 条码长度
	 */
	@ApiModelProperty(name = "barcodeLen", value = "条码长度")
	@Column
	protected Long barcodeLen;

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

	public String getItemNoCus() {
		return itemNoCus;
	}

	public void setItemNoCus(String itemNoCus) {
		this.itemNoCus = itemNoCus;
	}

	public String getItemNoInside() {
		return itemNoInside;
	}

	public void setItemNoInside(String itemNoInside) {
		this.itemNoInside = itemNoInside;
	}

	public Long getPositionBegin() {
		return positionBegin;
	}

	public void setPositionBegin(Long positionBegin) {
		this.positionBegin = positionBegin;
	}

	public Long getPositionEnd() {
		return positionEnd;
	}

	public void setPositionEnd(Long positionEnd) {
		this.positionEnd = positionEnd;
	}

	public String getChkString() {
		return chkString;
	}

	public void setChkString(String chkString) {
		this.chkString = chkString;
	}

	public Long getBarcodeLen() {
		return barcodeLen;
	}

	public void setBarcodeLen(Long barcodeLen) {
		this.barcodeLen = barcodeLen;
	}
}
