package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 物料基础信息表
 *
 */
@Entity(name = "Mtrial")
@Table(name = Mtrial.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Mtrial extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "basic_mtrial";
	    
	    /**
	     * 物料编码
	     */
	    @ApiModelProperty(name = "bsCode", value = "物料编码")
	    @Column(length = 50)
	    protected String bsCode;
	    
	    /**
	     * 物料信息名称
	     */
	    @ApiModelProperty(name = "bsName", value = "物料名称")
	    @Column(length = 50)
	    protected String bsName;
	    
	    /**
	     * 物料类别
	     */
	    @ApiModelProperty(name = "bsType", value = "物料类别")
	    @Column(length = 50)
	    protected String bsType;
	    
	    /**
	     * 物料单位
	     */
	    @ApiModelProperty(name = "bsUnit", value = "物料单位")
	    @Column(length = 15)
	    protected String bsUnit;
	    
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

		public String getBsType() {
			return bsType;
		}

		public void setBsType(String bsType) {
			this.bsType = bsType;
		}

		public String getBsUnit() {
			return bsUnit;
		}

		public void setBsUnit(String bsUnit) {
			this.bsUnit = bsUnit;
		}

		public Integer getBsStatus() {
			return bsStatus;
		}

		public void setBsStatus(Integer bsStatus) {
			this.bsStatus = bsStatus;
		}    
}
