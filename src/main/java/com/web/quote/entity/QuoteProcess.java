package com.web.quote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

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
	 * 工序顺序
	 */
	@ApiModelProperty(name = "bsCode", value = "工序顺序")
	@Column(length = 50)
	protected String bsCode;
}
