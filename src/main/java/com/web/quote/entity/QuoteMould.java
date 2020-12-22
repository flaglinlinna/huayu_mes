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
import com.web.basePrice.entity.MjProcFee;
import com.web.basePrice.entity.Proc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 模具清单维护
 *
 */
@Entity(name = "QuoteMould")
@Table(name = QuoteMould.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteMould extends BaseEntity {
	
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_quote_mould";
	
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
	 * Bom组件名称
	 */
	@ApiModelProperty(name = "bsName", value = "Bom组件名称")
	@Column(length = 100)
	protected String bsName;
	
	

	/**
     * 模具信息-模具成本
     */
    @ApiModelProperty(name="pkProcFee",value="模具信息-模具成本")
    @Column
    protected Long pkProcFee;

    @ApiModelProperty(name="mjProcFee",hidden=true,value="模具信息-模具成本")
    @ManyToOne
    @JoinColumn(name = "pkProcFee", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected MjProcFee mjProcFee;
	
	/**
	 * 模具编码
	 */
	@ApiModelProperty(name = "bsMoCode", value = "模具编码")
	@Column(length = 100)
	protected String bsMoCode;
	
	/**
	 * 模具名称
	 */
	@ApiModelProperty(name = "bsMoName", value = "模具名称")
	@Column(length = 100)
	protected String bsMoName;
    
	/**
	 * 模具成本
	 */
	@ApiModelProperty(name = "bsMoFee", value = "模具成本")
	protected BigDecimal bsMoFee;
	
	/**
	 * 实际报价
	 */
	@ApiModelProperty(name = "bsActQuote", value = "实际报价")
	protected BigDecimal bsActQuote;
	
	/**
	 * 参考报价
	 */
	@ApiModelProperty(name = "bsStQuote", value = "参考报价")
	protected BigDecimal bsStQuote;

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

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public String getBsMoCode() {
		return bsMoCode;
	}

	public void setBsMoCode(String bsMoCode) {
		this.bsMoCode = bsMoCode;
	}

	public String getBsMoName() {
		return bsMoName;
	}

	public void setBsMoName(String bsMoName) {
		this.bsMoName = bsMoName;
	}

	public BigDecimal getBsMoFee() {
		return bsMoFee;
	}

	public void setBsMoFee(BigDecimal bsMoFee) {
		this.bsMoFee = bsMoFee;
	}

	public BigDecimal getBsActQuote() {
		return bsActQuote;
	}

	public void setBsActQuote(BigDecimal bsActQuote) {
		this.bsActQuote = bsActQuote;
	}

	public BigDecimal getBsStQuote() {
		return bsStQuote;
	}

	public void setBsStQuote(BigDecimal bsStQuote) {
		this.bsStQuote = bsStQuote;
	}

	public Long getPkProcFee() {
		return pkProcFee;
	}

	public void setPkProcFee(Long pkProcFee) {
		this.pkProcFee = pkProcFee;
	}

	public MjProcFee getMjProcFee() {
		return mjProcFee;
	}

	public void setMjProcFee(MjProcFee mjProcFee) {
		this.mjProcFee = mjProcFee;
	}
	
	
}
