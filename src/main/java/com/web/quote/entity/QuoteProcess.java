package com.web.quote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.Proc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价报价工艺流程表
 *
 */
@Entity(name = "QuoteProcess")
@Table(name = QuoteProcess.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteProcess {
	
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
    @ApiModelProperty(name="pkQuoteBom",value="报价主表")
    @Column
    protected Long pkQuoteBom;

    @ApiModelProperty(name="quoteBom",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkQuoteBom", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected QuoteBom quoteBom;
    
    
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
     * 关联工作中心
     */
    @ApiModelProperty(name="pkBjWorkCenter",value="工作中心表")
    @Column
    protected Long pkBjWorkCenter;
    
    @ApiModelProperty(name="bjWorkCenter",hidden=true,value="工作中心表")
    @ManyToOne
    @JoinColumn(name = "pkBjWorkCenter", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected BjWorkCenter bjWorkCenter;
	
	/**
	 * 工序顺序
	 */
	@ApiModelProperty(name = "bsOrder", value = "工序顺序")
	@Column(length = 50)
	protected String bsOrder;
	
	/**
	 * 工序说明
	 */
	@ApiModelProperty(name = "bsExplain", value = "工序说明")
	@Column(length = 100)
	protected String bsExplain;
}
