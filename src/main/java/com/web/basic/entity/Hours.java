package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 工时基础信息表
 *
 */
@Entity(name = "Hours")
@Table(name = Hours.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Hours extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "basic_hours";
	    
	    /**
	     * 产品编码
	     */
	    @ApiModelProperty(name = "bsCode", value = "产品编码")
	    @Column(length = 15)
	    protected String bsCode;

	    /**
	     * 标准工时
	     */
	    @ApiModelProperty(name = "bsStdHrs", value = "标准工时")
	    @Column(length = 15)
	    protected String bsStdHrs;

		public String getBsCode() {
			return bsCode;
		}

		public void setBsCode(String bsCode) {
			this.bsCode = bsCode;
		}

		public String getBsStdHrs() {
			return bsStdHrs;
		}

		public void setBsStdHrs(String bsStdHrs) {
			this.bsStdHrs = bsStdHrs;
		}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("产品编码:").append(this.bsCode);
		sb.append(",标准工时:").append(this.bsStdHrs);
		return sb.toString();
	}
}
