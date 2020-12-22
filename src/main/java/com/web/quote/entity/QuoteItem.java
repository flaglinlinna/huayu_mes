package com.web.quote.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @ApiModelProperty(name="pkQuote",value="报价主表")
    @Column
    protected Long pkQuote;

    @ApiModelProperty(name="quote",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkQuote", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Quote quote;
	
    /**
   	 * 报价项目-类型
   	 * 1:item
	 * 2:mater
	 * 3:process
   	 */
   	@ApiModelProperty(name = "bsStyle", value = "报价项目-类型")
   	@Column(length = 50)
   	protected String bsStyle;
   	
    /**
	 * 报价项目-编码
	 */
	@ApiModelProperty(name = "bsCode", value = "报价项目-编码")
	@Column(length = 50)
	protected String bsCode;
	/**
	 * 报价项目名
	 */
	@ApiModelProperty(name = "bsName", value = "报价项目名")
	@Column(length = 150)
	protected String bsName;
	
	/**
	 * 待办人
	 */
	@ApiModelProperty(name = "bsPerson", value = "待办人")
	@Column(length = 100)
	protected String bsPerson;
	
	/**
     * 待办人Id
     */
	@ApiModelProperty(name = "toDoBy", value = "待办人Id")
    @Column(length = 30)
    protected Long toDoBy;
	
	/**
     * 开始时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsBegTime",value="开始时间")
	protected Date bsBegTime;
	
	
	/**
	 * 结束时间
	 */	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsEndTime",value="结束时间")
	protected Date bsEndTime;
	
	/**
	 * 项目进度状态（0未开始、1进行中、2已完成、3不需要填写）
	 */
	@ApiModelProperty(name = "bsStatus", value = "项目进度状态")
	@Column
	protected int bsStatus=0;

	

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

	public String getBsPerson() {
		return bsPerson;
	}

	public void setBsPerson(String bsPerson) {
		this.bsPerson = bsPerson;
	}

	public Date getBsBegTime() {
		return bsBegTime;
	}

	public void setBsBegTime(Date bsBegTime) {
		this.bsBegTime = bsBegTime;
	}

	public Date getBsEndTime() {
		return bsEndTime;
	}

	public void setBsEndTime(Date bsEndTime) {
		this.bsEndTime = bsEndTime;
	}

	public int getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(int bsStatus) {
		this.bsStatus = bsStatus;
	}

	public String getBsCode() {
		return bsCode;
	}

	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}

	public Long getToDoBy() {
		return toDoBy;
	}

	public void setToDoBy(Long toDoBy) {
		this.toDoBy = toDoBy;
	}

	public String getBsStyle() {
		return bsStyle;
	}

	public void setBsStyle(String bsStyle) {
		this.bsStyle = bsStyle;
	}
	
	
}
