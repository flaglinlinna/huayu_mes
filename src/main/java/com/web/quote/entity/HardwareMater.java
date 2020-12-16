package com.web.quote.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 五金材料信息表
 *
 */
@Entity(name = "HardwareMater")
@Table(name = HardwareMater.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class HardwareMater extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "PRICE_HARDWARE_MATER";

	/**
	 * 报价表Id
	 */
	@ApiModelProperty(name = "QuoteId", value = "报价表Id")
	@Column(length = 50)
	protected Long quoteId;

	/**
	 * 零件
	 */
	@ApiModelProperty(name = "component", value = "零件名称")
	@Column(length = 50)
	protected String component;

	/**
	 * 材料名称
	 */
	@ApiModelProperty(name = "materName", value = "材料名称")
	@Column(length = 50)
	protected String materName;

	/**
	 * 材料规格
	 */
	@ApiModelProperty(name = "model", value = "材料规格")
	@Column(length = 50)
	protected String model;

	/**
	 * 用量
	 */
	@ApiModelProperty(name = "qty", value = "用量")
	@Column(length = 50)
	protected BigDecimal qty;

	/**
	 * 单位
	 */
	@ApiModelProperty(name = "unit", value = "单位")
	@Column(length = 50)
	protected String unit;

	/**
	 * 基数
	 */
	@ApiModelProperty(name = "radix", value = "基数")
	@Column(length = 50)
	protected String radix;

	/**
	 * 供应商
	 */
	@ApiModelProperty(name = "supplier", value = "供应商")
	@Column(length = 50)
	protected String supplier;

	public Long getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getMaterName() {
		return materName;
	}

	public void setMaterName(String materName) {
		this.materName = materName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRadix() {
		return radix;
	}

	public void setRadix(String radix) {
		this.radix = radix;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
}
