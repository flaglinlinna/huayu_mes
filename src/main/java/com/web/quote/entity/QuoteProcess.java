package com.web.quote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;
import com.web.basePrice.entity.BjWorkCenter;
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
	 * 工序顺序
	 */
	@ApiModelProperty(name = "bsOrder", value = "工序顺序")
	protected int bsOrder;

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
	
}
