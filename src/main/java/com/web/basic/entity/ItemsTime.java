package com.web.basic.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 静置时间维护信息表
 *
 */
@Entity(name = "ItemsTime")
@Table(name = ItemsTime.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ItemsTime extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "MES_BASE_ITEMS_TIME";
	/**
	 * 物料编码
	 */
	@ApiModelProperty(name = "itemNo", value = "物料编码")
	@Column(length = 50)
	protected String itemNo;

	/**
	 * 静置时长(H)
	 */
	@ApiModelProperty(name = "time", value = "静置时长(H)")
	@Column(length = 50)
	protected BigDecimal itemTime;

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public BigDecimal getItemTime() {
		return itemTime;
	}

	public void setItemTime(BigDecimal itemTime) {
		this.itemTime = itemTime;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("物料编码:").append(this.itemNo);
		sb.append("静置时长(H):").append(this.itemTime);
		return sb.toString();
	}
}
