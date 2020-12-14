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
	
//	/**
//	 * 华勤物料编码
//	 */
//	@ApiModelProperty(name = "itemNoInside", value = "华勤物料编码")
//	@Column(length = 100)
//	protected String itemNoInside;

//	/**
//	 * 开始位置
//	 */
//	@ApiModelProperty(name = "positionBegin", value = "开始位置")
//	@Column
//	protected Long positionBegin;
	
//	/**
//	 * 结束位置
//	 */
//	@ApiModelProperty(name = "positionEnd", value = "结束位置")
//	@Column
//	protected Long positionEnd;
	



	/**
	 *  固定值
	 */
	@ApiModelProperty(name = "chkString", value = "固定值")
	@Column(length = 100)
	protected String fixValue;
//	FIX_VALUE
	/**
	 *  年份
	 */
	@ApiModelProperty(name = "fyear", value = "年份")
	@Column(length = 100)
	protected String fyear;

	/**
	 *  月
	 */
	@ApiModelProperty(name = "fmonth", value = "月")
	@Column(length = 100)
	protected String fmonth;

	/**
	 *  日
	 */
	@ApiModelProperty(name = "fyear", value = "日")
	@Column(length = 100)
	protected String fday;

	/**
	 *  流水号
	 */
	@ApiModelProperty(name = "serialNum", value = "流水号")
	@Column(length = 50)
	protected String serialNum;

	/**
	 *  流水号位数
	 */
	@ApiModelProperty(name = "serialLen", value = "流水号位数")
	@Column(length = 100)
	protected Integer serialLen;

	/**
	 *  条码样例
	 */
	@ApiModelProperty(name = "fsample", value = "条码样例")
	@Column(length = 100)
	protected String fsample;

//	/**
//	 * 条码长度
//	 */
//	@ApiModelProperty(name = "barcodeLen", value = "条码长度")
//	@Column
//	protected Long barcodeLen;
	
	//一下复写具体输出形式自己定
//    public String toString(){
//        return "客户表ID:" + this.custId + ", 物料表ID:"+this.itemId+",物料编码:"+this.itemNo+
//        		",客户物料编码:"+this.itemNoCus+",华勤物料编码:"+this.itemNoInside+",开始位置:"+this.positionBegin+
//        		",结束位置:"+positionEnd+",验证数据:"+chkString+",条码长度:"+barcodeLen;
//    }

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

	public String getFixValue() {
		return fixValue;
	}

	public void setFixValue(String fixValue) {
		this.fixValue = fixValue;
	}

	public String getFyear() {
		return fyear;
	}

	public void setFyear(String fyear) {
		this.fyear = fyear;
	}

	public String getFmonth() {
		return fmonth;
	}

	public void setFmonth(String fmonth) {
		this.fmonth = fmonth;
	}

	public String getFday() {
		return fday;
	}

	public void setFday(String fday) {
		this.fday = fday;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Integer getSerialLen() {
		return serialLen;
	}

	public void setSerialLen(Integer serialLen) {
		this.serialLen = serialLen;
	}

	public String getFsample() {
		return fsample;
	}

	public void setFsample(String fsample) {
		this.fsample = fsample;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("客户id:").append(this.custId);
		sb.append(",物料编码:").append(this.itemNo);
		sb.append(",客户物料编码:").append(this.itemNoCus);
		sb.append(",固定值:").append(this.fixValue);
		sb.append(",年:").append(this.fyear);
		sb.append(",月:").append(this.fmonth);
		sb.append(",日:").append(this.fday);
		sb.append(",流水号:").append(this.serialNum);
		sb.append(",位数:").append(this.serialLen);
		sb.append(",样例:").append(this.fsample);
		return sb.toString();
	}
}
