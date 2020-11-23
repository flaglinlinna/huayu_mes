package com.web.basic.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 生产异常原因信息表
 *
 */
@Entity(name = "ProdErr")
@Table(name = ProdErr.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProdErr extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "MES_BASE_PROD_ERR";
	/**
	 * 异常代码
	 */
	@ApiModelProperty(name = "errCode", value = "异常代码")
	@Column(length = 50)
	protected String errCode;

	/**
	 * 异常类型
	 */
	@ApiModelProperty(name = "errName", value = "异常类型")
	@Column(length = 50)
	protected String errName;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrName() {
		return errName;
	}

	public void setErrName(String errName) {
		this.errName = errName;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("异常代码:").append(this.errCode);
		sb.append("异常类型:").append(this.errName);
		return sb.toString();
	}
}
