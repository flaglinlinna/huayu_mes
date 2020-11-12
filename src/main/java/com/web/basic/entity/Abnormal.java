package com.web.basic.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 异常类别基础信息表
 *
 */
@Entity(name = "Abnormal")
@Table(name = Abnormal.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Abnormal extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	   
	 public static final String TABLE_NAME = "MES_BASE_ABNORMAL_TYPE";
	    /**
	     * 异常代码
	     */
	    @ApiModelProperty(name = "abnormalCode", value = "异常代码")
	    @Column(length = 50)
	    protected String abnormalCode;

	    /**
	     * 异常类型
	     */
	    @ApiModelProperty(name = "abnormalType", value = "异常类型")
	    @Column(length = 50)
	    protected String abnormalType;


	public String getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(String abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

	public String getAbnormalType() {
		return abnormalType;
	}

	public void setAbnormalType(String abnormalType) {
		this.abnormalType = abnormalType;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("异常代码:").append(this.abnormalCode);
		sb.append("异常类型:").append(this.abnormalType);
		return sb.toString();
	}
}
