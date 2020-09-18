package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 客户基础信息表
 *
 */
@Entity(name = "Client")
@Table(name = Client.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Client extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "basic_client";
	    
	    /**
	     * 客户编码
	     */
	    @ApiModelProperty(name = "bsCode", value = "客户编码")
	    @Column(length = 50)
	    protected String bsCode;

	    /**
	     * 客户全称
	     */
	    @ApiModelProperty(name = "bsName", value = "客户全称")
	    @Column(length = 50)
	    protected String bsName;
	    
	    /**
	     * 客户简称
	     */
	    @ApiModelProperty(name = "bsNameSmpl", value = "客户简称")
	    @Column(length = 50)
	    protected String bsNameSmpl;
	        

		public String getBsCode() {
			return bsCode;
		}

		public void setBsCode(String bsCode) {
			this.bsCode = bsCode;
		}

		public String getBsName() {
			return bsName;
		}

		public void setBsName(String bsName) {
			this.bsName = bsName;
		}

		public String getBsNameSmpl() {
			return bsNameSmpl;
		}

		public void setBsNameSmpl(String bsNameSmpl) {
			this.bsNameSmpl = bsNameSmpl;
		}
}
