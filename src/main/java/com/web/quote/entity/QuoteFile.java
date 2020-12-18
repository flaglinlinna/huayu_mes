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
 * 产品资料信息表
 *
 */
@Entity(name = "QuoteFile")
@Table(name = QuoteFile.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteFile extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "PRICE_QUOTE_FILE";

	/**
	 * 关联主表
	 */
	@ApiModelProperty(name="pkQuote",value="报价主表")
	@Column
	protected Long pkQuote;

	/**
	 * 文件名称
	 */
	@ApiModelProperty(name = "bsFileName", value = "文件名称")
	@Column(length = 200)
	protected String bsFileName;

	/**
	 * 文件表id
	 */
	@ApiModelProperty(name = "pkFileId", value = "材料名称")
	@Column(length = 200)
	protected String pkFileId;

	public Long getPkQuote() {
		return pkQuote;
	}

	public void setPkQuote(Long pkQuote) {
		this.pkQuote = pkQuote;
	}

	public String getBsFileName() {
		return bsFileName;
	}

	public void setBsFileName(String bsFileName) {
		this.bsFileName = bsFileName;
	}

	public String getPkFileId() {
		return pkFileId;
	}

	public void setPkFileId(String pkFileId) {
		this.pkFileId = pkFileId;
	}
}
