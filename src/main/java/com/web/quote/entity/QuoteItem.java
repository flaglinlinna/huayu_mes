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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价单项目
 *
 */

@Entity(name = "QuoteItem")
@Table(name = QuoteItem.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteItem extends BaseEntity{
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_quote_item";
	
	/**
     * 关联主表
     */
    @ApiModelProperty(name="pkQuoteId",value="报价主表")
    @Column
    protected Long pkQuoteId;

    @ApiModelProperty(name="quote",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkQuoteId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Quote quote;
	
	
	/**
	 * 报价项目名
	 */
	@ApiModelProperty(name = "bsName", value = "报价项目名")
	@Column(length = 50)
	protected String bsName;
	
	/**
	 * 待办人
	 */
	@ApiModelProperty(name = "bsPerson", value = "待办人")
	@Column(length = 50)
	protected String bsPerson;
	
	/**
	 * 开始时间
	 */
	@ApiModelProperty(name = "bsBegTime", value = "开始时间")
	@Column(length = 50)
	protected String bsBegTime;
	
	/**
	 * 结束时间
	 */
	@ApiModelProperty(name = "bsEndTime", value = "结束时间")
	@Column(length = 50)
	protected String bsEndTime;
	
	/**
	 * 项目进度状态（未开始、未完成、已完成）
	 */
	@ApiModelProperty(name = "bsStatus", value = "项目进度状态")
	@Column(length = 50)
	protected String bsStatus;

	public Long getPkQuoteId() {
		return pkQuoteId;
	}

	public void setPkQuoteId(Long pkQuoteId) {
		this.pkQuoteId = pkQuoteId;
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

	public String getBsPerson() {
		return bsPerson;
	}

	public void setBsPerson(String bsPerson) {
		this.bsPerson = bsPerson;
	}

	public String getBsBegTime() {
		return bsBegTime;
	}

	public void setBsBegTime(String bsBegTime) {
		this.bsBegTime = bsBegTime;
	}

	public String getBsEndTime() {
		return bsEndTime;
	}

	public void setBsEndTime(String bsEndTime) {
		this.bsEndTime = bsEndTime;
	}

	public String getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(String bsStatus) {
		this.bsStatus = bsStatus;
	}
}
