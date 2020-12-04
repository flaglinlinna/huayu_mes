package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 外购物料类型
 *
 */
@Entity(name = "ItemTypeWg")
@Table(name = ItemTypeWg.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ItemTypeWg extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_ITEM_TYPE_WG";
	    
	    /**
	     * 物料类型
	     */
	    @ApiModelProperty(name = "itemType", value = "物料类型")
	    @Column(length = 50)
	    protected String itemType;

		public String getItemType() {
			return itemType;
		}

		public void setItemType(String itemType) {
			this.itemType = itemType;
		}
}
