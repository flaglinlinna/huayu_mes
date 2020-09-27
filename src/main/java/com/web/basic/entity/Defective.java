package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	   
	 public static final String TABLE_NAME = "MES_BASE_DEFECT_TYPE";
	    /**
	     * 不良类别编码
	     */
	    @ApiModelProperty(name = "defectTypeCode", value = "不良类别编码")
	    @Column(length = 50)
	    protected String defectTypeCode;

	    /**
	     * 不良类别名称
	     */
	    @ApiModelProperty(name = "defectTypeName", value = "不良类别名称")
	    @Column(length = 50)
	    protected String defectTypeName;
	    
	    /**
	     * 状态（0：正常 / 1：禁用）
	     */
	    @ApiModelProperty(name = "checkStatus", value = "状态（0：正常 / 1：禁用）")
	    @Column
	    protected Integer checkStatus = 0;

		public String getDefectTypeCode() {
			return defectTypeCode;
		}

		public void setDefectTypeCode(String defectTypeCode) {
			this.defectTypeCode = defectTypeCode;
		}

		public String getDefectTypeName() {
			return defectTypeName;
		}

		public void setDefectTypeName(String defectTypeName) {
			this.defectTypeName = defectTypeName;
		}

		public Integer getCheckStatus() {
			return checkStatus;
		}

		public void setCheckStatus(Integer checkStatus) {
			this.checkStatus = checkStatus;
		}

		
}
