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
@Table(name = ProductMater.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProductMater extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "PRICE_product_MATER";
	
	/**
	 * 类型
	 * 五金:hardware
	 * 注塑:molding
	 * 表面处理:surface
	 * 组装:assemble
	 */
	@ApiModelProperty(name = "bsType", value = "零件名称")
	@Column(length = 50)
	protected String bsType;
	
	/**
	 * 关联主表
	 */
	@ApiModelProperty(name="pkQuote",value="报价主表")
	@Column
	protected Long pkQuote;

	/**
	 * 零件
	 */
	@ApiModelProperty(name = "bsComponent", value = "零件名称")
	@Column(length = 200)
	protected String bsComponent;

	/**
	 * 材料名称
	 */
	@ApiModelProperty(name = "bsMaterName", value = "材料名称")
	@Column(length = 200)
	protected String bsMaterName;

	/**
	 * 材料规格
	 */
	@ApiModelProperty(name = "bsModel", value = "材料规格")
	@Column(length = 50)
	protected String bsModel;

	/**
	 * 用量
	 */
	@ApiModelProperty(name = "bsQty", value = "用量")
	@Column(length = 50)
	protected BigDecimal bsQty;

	/**
	 * 单位
	 */
	@ApiModelProperty(name = "unit", value = "单位")
	@Column(length = 50)
	protected String bsUnit;

	/**
	 * 基数
	 */
	@ApiModelProperty(name = "bsRadix", value = "基数")
	@Column(length = 50)
	protected String bsRadix;

	/**
	 * 供应商
	 */
	@ApiModelProperty(name = "supplier", value = "供应商")
	@Column(length = 200)
	protected String bsSupplier;

	public Long getPkQuote() {
		return pkQuote;
	}

	public void setPkQuote(Long pkQuote) {
		this.pkQuote = pkQuote;
	}


	public String getBsComponent() {
		return bsComponent;
	}

	public void setBsComponent(String bsComponent) {
		this.bsComponent = bsComponent;
	}

	public String getBsMaterName() {
		return bsMaterName;
	}

	public void setBsMaterName(String bsMaterName) {
		this.bsMaterName = bsMaterName;
	}

	public String getBsModel() {
		return bsModel;
	}

	public void setBsModel(String bsModel) {
		this.bsModel = bsModel;
	}

	public BigDecimal getBsQty() {
		return bsQty;
	}

	public void setBsQty(BigDecimal bsQty) {
		this.bsQty = bsQty;
	}

	public String getBsUnit() {
		return bsUnit;
	}

	public void setBsUnit(String bsUnit) {
		this.bsUnit = bsUnit;
	}

	public String getBsRadix() {
		return bsRadix;
	}

	public void setBsRadix(String bsRadix) {
		this.bsRadix = bsRadix;
	}

    public String getBsSupplier() {
        return bsSupplier;
    }

    public void setBsSupplier(String bsSupplier) {
        this.bsSupplier = bsSupplier;
    }
}
