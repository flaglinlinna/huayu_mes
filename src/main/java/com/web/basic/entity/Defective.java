package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 不良类别基础信息表
 *
 */
@Entity(name = "Defective")
@Table(name = Defective.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Defective extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "basic_chk_bad";
	    
	    /**
	     * 不良类别编码
	     */
	    @ApiModelProperty(name = "bsCode", value = "不良类别编码")
	    @Column(length = 50)
	    protected String bsCode;

	    /**
	     * 不良类别名称
	     */
	    @ApiModelProperty(name = "bsName", value = "不良类别名称")
	    @Column(length = 50)
	    protected String bsName;
	    
	    /**
	     * 状态（0：正常 / 1：禁用）
	     */
	    @ApiModelProperty(name = "bsStatus", value = "状态（0：正常 / 1：禁用）")
	    @Column
	    protected Integer bsStatus = 0;

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

		public Integer getBsStatus() {
			return bsStatus;
		}

		public void setBsStatus(Integer bsStatus) {
			this.bsStatus = bsStatus;
		}    
}
