package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品类型维护表
 *
 */
@Entity(name = "ProdTyp")
@Table(name = ProdTyp.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProdTyp extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_base_PROD_TYP";
	    
	    /**
	     * 产品类型
	     */
	    @ApiModelProperty(name = "productType", value = "产品类型")
	    @Column(length = 20)
	    protected String productType;

	    /**
	     * 是否有效 1(有效)/0（无效）
	     */
	    @ApiModelProperty(name = "enabled", value = "是否有效 1(有效)/0（无效）")
	    @Column(length = 10)
	    protected Integer enabled = 1;

		public String getProductType() {
			return productType;
		}

		public void setProductType(String productType) {
			this.productType = productType;
		}

		public int getEnabled() {
			return enabled;
		}

		public void setEnabled(int enabled) {
			this.enabled = enabled;
		}
}
