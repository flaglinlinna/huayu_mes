package com.web.quote.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;
import com.web.basePrice.entity.Proc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价工艺流程表
 *
 */
@Entity(name = "QuoteProcess")
@Table(name = QuoteProcess.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteProcess extends BaseEntity {
	
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_quote_process";
	
	/**
     * 状态
     * 0：草稿，1:完成
     */
	@Column
	@ApiModelProperty(name="bsStatus",value="状态")
    protected int bsStatus = 0;
	
	/**
     * 是否需要填写
     * 0：需要，1:不需要
     */
	@Column
	@ApiModelProperty(name="bsNeed",value="是否需要填写")
    protected int bsNeed = 0;
	
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
     * 关联外购件清单（报价BOM清单）
     */
    @ApiModelProperty(name="pkQuoteBom",value="BOM清单表")
    @Column
    protected Long pkQuoteBom;

    @ApiModelProperty(name="quoteBom",hidden=true,value="BOM清单表")
    @ManyToOne
    @JoinColumn(name = "pkQuoteBom", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected QuoteBom quoteBom;
    
    /**
	 * Bom零件名称
	 */
	@ApiModelProperty(name = "bsName", value = "Bom零件名称")
	@Column(length = 100)
	protected String bsName;
    
    /**
     * 关联工序表
     */
    @ApiModelProperty(name="pkProc",value="工序表")
    @Column
    protected Long pkProc;
    
    @ApiModelProperty(name="proc",hidden=true,value="工序表")
    @ManyToOne
    @JoinColumn(name = "pkProc", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Proc proc;

	 /**
	 * 组件名称
	 */
	@ApiModelProperty(name = "bsElement", value = "组件名称")
	@Column(length = 500)
	protected String bsElement;
    
	
	/**
	 * 工序顺序
	 */
	@ApiModelProperty(name = "bsOrder", value = "工序顺序")
	protected int bsOrder;
	
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
	 * 材料名称
	 */
	@ApiModelProperty(name = "bsMaterName", value = "材料名称")
	@Column(length = 200)
	protected String bsMaterName;

	/**
	 * 材料名称列表
	 */
	@ApiModelProperty(name = "bsMaterNameList", value = "材料名称列表")
	@Column(length = 200)
	protected String bsMaterNameList;

	/**
	 * 分组名称
	 */
	@ApiModelProperty(name = "bsGroups", value = "分组名称")
	@Column(length = 100)
	protected String bsGroups;


	/**
	 * 分组名称列表
	 */
	@ApiModelProperty(name = "bsGroupsList", value = "分组名称列表")
	@Column(length = 1000)
	protected String bsGroupsList;

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

	public Long getPkQuoteBom() {
		return pkQuoteBom;
	}

	public void setPkQuoteBom(Long pkQuoteBom) {
		this.pkQuoteBom = pkQuoteBom;
	}

	public QuoteBom getQuoteBom() {
		return quoteBom;
	}

	public void setQuoteBom(QuoteBom quoteBom) {
		this.quoteBom = quoteBom;
	}

	public Long getPkProc() {
		return pkProc;
	}

	public void setPkProc(Long pkProc) {
		this.pkProc = pkProc;
	}

	public Proc getProc() {
		return proc;
	}

	public void setProc(Proc proc) {
		this.proc = proc;
	}

	

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public int getBsOrder() {
		return bsOrder;
	}

	public void setBsOrder(int bsOrder) {
		this.bsOrder = bsOrder;
	}

	public int getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(int bsStatus) {
		this.bsStatus = bsStatus;
	}

	public int getBsNeed() {
		return bsNeed;
	}

	public void setBsNeed(int bsNeed) {
		this.bsNeed = bsNeed;
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

	public String getBsElement() {
		return bsElement;
	}

	public void setBsElement(String bsElement) {
		this.bsElement = bsElement;
	}

	public String getBsMaterName() {
		return bsMaterName;
	}

	public void setBsMaterName(String bsMaterName) {
		this.bsMaterName = bsMaterName;
	}

	public String getBsMaterNameList() {
		return bsMaterNameList;
	}

	public void setBsMaterNameList(String bsMaterNameList) {
		this.bsMaterNameList = bsMaterNameList;
	}

	public String getBsGroups() {
		return bsGroups;
	}

	public void setBsGroups(String bsGroups) {
		this.bsGroups = bsGroups;
	}

	public String getBsGroupsList() {
		return bsGroupsList;
	}

	public void setBsGroupsList(String bsGroupsList) {
		this.bsGroupsList = bsGroupsList;
	}
}
