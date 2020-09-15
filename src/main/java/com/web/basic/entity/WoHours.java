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
@Entity(name = "WoHours")
@Table(name = WoHours.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class WoHours extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "basic_wo_hours";
	    
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
}
