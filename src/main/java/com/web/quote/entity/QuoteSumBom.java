package com.web.quote.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.entity.Unit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价汇总BOM清单
 *
 */
@Entity(name = "QuoteSumBom")
@Table(name = QuoteSumBom.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteSumBom extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "price_quote_sum_bom";

	/**
     * 关联主表
     */
    @ApiModelProperty(name="pkQuote",value="报价主表")
    @Column
    protected Long pkQuote;

    @ApiModelProperty(name="quote",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkQuote", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Quote quote;
    
    /**
     * 关联报价工作中心
     */
    @ApiModelProperty(name="pkBjWorkCenter",value="关联报价工作中心")
    @Column
    protected Long pkBjWorkCenter;
    
    @ApiModelProperty(name="wc",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkBjWorkCenter", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected BjWorkCenter wc;


    /**
     * 关联单位
     */
    @ApiModelProperty(name="pkUnit",value="关联单位")
    @Column
    protected Long pkUnit;
    
    @ApiModelProperty(name="unit",hidden=true,value="单位")
    @ManyToOne
    @JoinColumn(name = "pkUnit", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Unit unit;

    /**
	 * 物料编码-虚拟
	 */
	@ApiModelProperty(name = "bsItemCode", value = "物料编码")
	@Column(length = 50)
	protected String bsItemCode;
	
	/**
	 * 物料编码-真实
	 */
	@ApiModelProperty(name = "bsItemCodeReal", value = "物料编码-真实")
	@Column(length = 50)
	protected String bsItemCodeReal;

	/**
	 * 组件名称
	 */
	@ApiModelProperty(name = "bsElement", value = "组件名称")
	@Column(length = 150)
	protected String bsElement;
	
	/**
	 * 零件名称
	 */
	@ApiModelProperty(name = "bsComponent", value = "零件名称")
	@Column(length = 150)
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
	@Column(length = 200)
	protected String bsModel;


	/**
	 * 用量
	 */
	@ApiModelProperty(name = "bsQty", value = "用量")
	@Column(length = 50)
	protected BigDecimal bsQty;
	
	
	
	/**
	 * 人工费率（元/小时）
	 */
	@ApiModelProperty(name = "feeLh", value = "人工费率（元/小时）")
	@Column(length = 50)
	protected BigDecimal bsFeeLh;

	/**
	 * 制费费率（元/小时）
	 */
	@ApiModelProperty(name = "feeMh", value = "制费费率（元/小时）")
	@Column(length = 50)
	protected BigDecimal bsFeeMh;
	
	
	/**
	 * 人工费率（元/小时）
	 */
	@ApiModelProperty(name = "bsFeeLhAll", value = "人工费率（元/小时）")
	@Column(length = 50)
	protected BigDecimal bsFeeLhAll;

	/**
	 * 制费费率（元/小时）
	 */
	@ApiModelProperty(name = "bsFeeMhAll", value = "制费费率（元/小时）")
	@Column(length = 50)
	protected BigDecimal bsFeeMhAll;
	
	
	/**
	 * 材料费用
	 */
	@ApiModelProperty(name = "bsFee", value = "材料费用")
	@Column(length = 50)
	protected BigDecimal bsFeeItemAll;
	

	/**
	 * 外协费用
	 */
	@ApiModelProperty(name = "bsFeeOut", value = "外协费用")
	@Column(length = 50)
	protected BigDecimal bsFeeOut;


	public Long getPkQuote() {
		return pkQuote;
	}


	public void setPkQuote(Long pkQuote) {
		this.pkQuote = pkQuote;
	}


	public Quote getQuote() {
		return quote;
	}


	public void setQuote(Quote quote) {
		this.quote = quote;
	}


	public Long getPkBjWorkCenter() {
		return pkBjWorkCenter;
	}


	public void setPkBjWorkCenter(Long pkBjWorkCenter) {
		this.pkBjWorkCenter = pkBjWorkCenter;
	}


	public BjWorkCenter getWc() {
		return wc;
	}


	public void setWc(BjWorkCenter wc) {
		this.wc = wc;
	}


	public Long getPkUnit() {
		return pkUnit;
	}


	public void setPkUnit(Long pkUnit) {
		this.pkUnit = pkUnit;
	}


	public Unit getUnit() {
		return unit;
	}


	public void setUnit(Unit unit) {
		this.unit = unit;
	}


	public String getBsItemCode() {
		return bsItemCode;
	}


	public void setBsItemCode(String bsItemCode) {
		this.bsItemCode = bsItemCode;
	}


	public String getBsItemCodeReal() {
		return bsItemCodeReal;
	}


	public void setBsItemCodeReal(String bsItemCodeReal) {
		this.bsItemCodeReal = bsItemCodeReal;
	}


	public String getBsElement() {
		return bsElement;
	}


	public void setBsElement(String bsElement) {
		this.bsElement = bsElement;
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


	public BigDecimal getBsFeeLh() {
		return bsFeeLh;
	}


	public void setBsFeeLh(BigDecimal bsFeeLh) {
		this.bsFeeLh = bsFeeLh;
	}


	public BigDecimal getBsFeeMh() {
		return bsFeeMh;
	}


	public void setBsFeeMh(BigDecimal bsFeeMh) {
		this.bsFeeMh = bsFeeMh;
	}


	public BigDecimal getBsFeeLhAll() {
		return bsFeeLhAll;
	}


	public void setBsFeeLhAll(BigDecimal bsFeeLhAll) {
		this.bsFeeLhAll = bsFeeLhAll;
	}


	public BigDecimal getBsFeeMhAll() {
		return bsFeeMhAll;
	}


	public void setBsFeeMhAll(BigDecimal bsFeeMhAll) {
		this.bsFeeMhAll = bsFeeMhAll;
	}


	public BigDecimal getBsFeeItemAll() {
		return bsFeeItemAll;
	}


	public void setBsFeeItemAll(BigDecimal bsFeeItemAll) {
		this.bsFeeItemAll = bsFeeItemAll;
	}


	public BigDecimal getBsFeeOut() {
		return bsFeeOut;
	}


	public void setBsFeeOut(BigDecimal bsFeeOut) {
		this.bsFeeOut = bsFeeOut;
	}
	
	
}
