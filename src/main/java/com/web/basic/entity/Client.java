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
	    public static final String TABLE_NAME = "MES_base_customer";
	    
	    /**
	     * 客户编码
	     */
	    @ApiModelProperty(name = "custNo", value = "客户编码")
	    @Column(length = 50)
	    //@Column(columnDefinition = "varchar(50) comment '客户编码'")
	    protected String custNo;

	    /**
	     * 客户全称
	     */
	    @ApiModelProperty(name = "custName", value = "客户全称")
	    @Column(length = 50)
	    protected String custName;
	    
	    /**
	     * 客户简称
	     */
	    @ApiModelProperty(name = "custNameS", value = "客户简称")
	    @Column(length = 50)
	    protected String custNameS;

		public String getCustNo() {
			return custNo;
		}

		public void setCustNo(String custNo) {
			this.custNo = custNo;
		}

		public String getCustName() {
			return custName;
		}

		public void setCustName(String custName) {
			this.custName = custName;
		}

		public String getCustNameS() {
			return custNameS;
		}

		public void setCustNameS(String custNameS) {
			this.custNameS = custNameS;
		}
	        
	    
}
