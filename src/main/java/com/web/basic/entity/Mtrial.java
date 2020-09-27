package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
	    @Column(length = 15)
	    protected String itemUnit;
	    
	    /**
	     * 状态（0：正常 / 1：禁用）
	     */
	    @ApiModelProperty(name = "checkStatus", value = "状态（0：正常 / 1：禁用）")
	    @Column
	    protected Integer checkStatus = 0;

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

		  
}
