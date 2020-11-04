package com.web.baseInfo.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 产品利润率维护表
 *
 */
@Entity(name = "ProfitProd")
@Table(name = ProfitProd.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProfitProd extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_PROFIT_PROD";
	    
	    /**
	     * 产品类型
	     */
	    @ApiModelProperty(name = "productType", value = "产品类型")
	    @Column(length = 50)
	    protected String productType;

	    /**
	     * 机种类型
	     */
	    @ApiModelProperty(name = "itemType", value = "机种类型")
	    @Column(length = 50)
	    protected String itemType;

		/**
		 * 毛利率
		 */
		@ApiModelProperty(name = "profitRateGs", value = "毛利率")
		@Column(length = 20)
		protected BigDecimal profitRateGs;

		/**
		 * 是否有效
		 */
		@ApiModelProperty(name = "unitName", value = "单位名称")
		@Column(length = 10)
		protected int enabled = 1;

		public String getProductType() {
			return productType;
		}

		public void setProductType(String productType) {
			this.productType = productType;
		}

		public String getItemType() {
			return itemType;
		}

		public void setItemType(String itemType) {
			this.itemType = itemType;
		}

		public BigDecimal getProfitRateGs() {
			return profitRateGs;
		}

		public void setProfitRateGs(BigDecimal profitRateGs) {
			this.profitRateGs = profitRateGs;
		}

		public int getEnabled() {
			return enabled;
		}

		public void setEnabled(int enabled) {
			this.enabled = enabled;
		}
}
