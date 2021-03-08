package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 物料基础信息表
 *
 */
@Entity(name = "Mtrial")
@Table(name = Mtrial.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Mtrial extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "MES_base_ITEMS";
	    
	    /**
	     * 物料编码
	     */
	    @ApiModelProperty(name = "itemNo", value = "物料编码")
	    @Column(length = 50)
	    protected String itemNo;
	    
	    /**
	     * 物料信息名称
	     */
	    @ApiModelProperty(name = "itemName", value = "物料名称")
	    @Column(length = 500)
	    protected String itemName;
	    
	    /**
	     * 物料简称
	     */
	    @ApiModelProperty(name = "itemNameS", value = "物料简称")
	    @Column(length = 200)
	    protected String itemNameS;
	    
	    /**
	     * 物料规格
	     */
	    @ApiModelProperty(name = "itemModel", value = "物料规格")
	    @Column(length = 100)
	    protected String itemModel;

	    
	    
	    /**
	     * 物料类别
	     */
	    @ApiModelProperty(name = "itemType", value = "物料类别")
	    @Column(length = 50)
	    protected String itemType;
	    
	    /**
	     * 物料单位
	     */
	    @ApiModelProperty(name = "itemUnit", value = "物料单位")
	    @Column(length = 5)
	    protected String itemUnit;
	    
	    /**
	     * 状态（1：正常 / 0：禁用）
	     */
	    @ApiModelProperty(name = "checkStatus", value = "状态（1：正常 / 0：禁用）")
	    @Column
	    protected Integer checkStatus = 1;

		/**
		 * 产能(PCS/H)
		 */
		@ApiModelProperty(name = "产能(PCS/H)", value = "1")
		@Column(length = 20)
		protected BigDecimal  capacity;

		/**
		 * 人力(人)
		 */
		@ApiModelProperty(name = "人力(人)", value = "1")
		@Column(length = 20)
		protected BigDecimal manpower;

		/**
		 * 单人产能(H/人)
		 */
		@ApiModelProperty(name = "单人产能(H/人) ", value = "0")
		@Column(length = 20)
		protected BigDecimal capacityUnit;

		public String getItemNo() {
			return itemNo;
		}

		public void setItemNo(String itemNo) {
			this.itemNo = itemNo;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getItemNameS() {
			return itemNameS;
		}

		public void setItemNameS(String itemNameS) {
			this.itemNameS = itemNameS;
		}

		public String getItemModel() {
			return itemModel;
		}

		public void setItemModel(String itemModel) {
			this.itemModel = itemModel;
		}

		public String getItemType() {
			return itemType;
		}

		public void setItemType(String itemType) {
			this.itemType = itemType;
		}

		public String getItemUnit() {
			return itemUnit;
		}

		public void setItemUnit(String itemUnit) {
			this.itemUnit = itemUnit;
		}

		public Integer getCheckStatus() {
			return checkStatus;
		}

		public void setCheckStatus(Integer checkStatus) {
			this.checkStatus = checkStatus;
		}

		public BigDecimal getCapacity() {
			return capacity;
		}

		public void setCapacity(BigDecimal capacity) {
			this.capacity = capacity;
		}

		public BigDecimal getManpower() {
			return manpower;
		}

		public void setManpower(BigDecimal manpower) {
			this.manpower = manpower;
		}

		public BigDecimal getCapacityUnit() {
			return capacityUnit;
		}

		public void setCapacityUnit(BigDecimal capacityUnit) {
			this.capacityUnit = capacityUnit;
		}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("物料编码:").append(this.itemNo);
		sb.append(",物料名称:").append(this.itemName);
		sb.append(",物料简称:").append(this.itemNameS);
		sb.append(",物料规格:").append(this.itemModel);
		sb.append(",物料类别:").append(this.itemType);
		sb.append(",物料单位:").append(this.itemUnit);
		sb.append(",状态:").append(this.checkStatus==0?"禁用":"正常");
		return sb.toString();
	}
}
