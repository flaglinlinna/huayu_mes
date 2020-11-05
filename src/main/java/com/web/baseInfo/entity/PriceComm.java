package com.web.baseInfo.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 基本单位表
 *
 */
@Entity(name = "PriceComm")
@Table(name = PriceComm.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class PriceComm extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_PRICE_COMM";
	    
	    /**
	     * 物料名称
	     */
	    @ApiModelProperty(name = "itemName", value = "物料名称")
	    @Column(length = 50)
	    protected String itemName;

	    /**
	     * 价格档位
	     */
	    @ApiModelProperty(name = "rangePrice", value = "价格档位")
	    @Column(length = 30)
	    protected String rangePrice;

		/**
		 * 单价
		 */
		@ApiModelProperty(name = "priceUn", value = "单价")
		@Column(length = 10)
		protected String priceUn;

		/**
		 * 单位id
		 */
		@ApiModelProperty(name = "unitId", value = "单位id")
		@Column(length = 30)
		protected String unitId;

		/**
		 * 备选供应商
		 */
		@ApiModelProperty(name = "alternativeSuppliers", value = "备选供应商")
		@Column(length = 200)
		protected String alternativeSuppliers;

		/**
		 * 是否有效
		 */
		@ApiModelProperty(name = "unitName", value = "单位名称")
		@Column(length = 10)
		protected int enabled = 1;

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getRangePrice() {
			return rangePrice;
		}

		public void setRangePrice(String rangePrice) {
			this.rangePrice = rangePrice;
		}

		public String getPriceUn() {
			return priceUn;
		}

		public void setPriceUn(String priceUn) {
			this.priceUn = priceUn;
		}

		public String getUnitId() {
			return unitId;
		}

		public void setUnitId(String unitId) {
			this.unitId = unitId;
		}

		public String getAlternativeSuppliers() {
			return alternativeSuppliers;
		}

		public void setAlternativeSuppliers(String alternativeSuppliers) {
			this.alternativeSuppliers = alternativeSuppliers;
		}

		public int getEnabled() {
			return enabled;
		}

		public void setEnabled(int enabled) {
			this.enabled = enabled;
		}
}
