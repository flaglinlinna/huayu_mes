package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 不良 基础信息表
 *
 */
@Entity(name = "DefectiveDetail")
@Table(name = DefectiveDetail.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class DefectiveDetail extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "MES_BASE_DEFECT_DET";
	    
	    
	    /**
	      * 关联不良类别的ID
	      */
	     @ApiModelProperty(name="defectTypeId",value="不良类别ID")
	     @Column
	     protected Long defectTypeId;

	     @ApiModelProperty(name="defective",hidden=true,value="不良类别ID")
	     @ManyToOne
	     @JoinColumn(name = "defectTypeId", insertable = false, updatable = false)
	     @NotFound(action = NotFoundAction.IGNORE)
	     protected Defective defective;
	    
	    
	    /**
	     * 不良 编码
	     */
	    @ApiModelProperty(name = "defectCode", value = "不良 编码")
	    @Column(length = 50)
	    protected String defectCode;

	    /**
	     * 不良 名称
	     */
	    @ApiModelProperty(name = "defectName", value = "不良 名称")
	    @Column(length = 50)
	    protected String defectName;
	    
	    /**
	     * 状态（0：正常 / 1：禁用）
	     */
	    @ApiModelProperty(name = "checkStatus", value = "状态（0：正常 / 1：禁用）")
	    @Column
	    protected Integer checkStatus = 0;

		public Long getDefectTypeId() {
			return defectTypeId;
		}

		public void setDefectTypeId(Long defectTypeId) {
			this.defectTypeId = defectTypeId;
		}

		public Defective getDefective() {
			return defective;
		}

		public void setDefective(Defective defective) {
			this.defective = defective;
		}

		public String getDefectCode() {
			return defectCode;
		}

		public void setDefectCode(String defectCode) {
			this.defectCode = defectCode;
		}

		public String getDefectName() {
			return defectName;
		}

		public void setDefectName(String defectName) {
			this.defectName = defectName;
		}

		public Integer getCheckStatus() {
			return checkStatus;
		}

		public void setCheckStatus(Integer checkStatus) {
			this.checkStatus = checkStatus;
		}
	    
		

		  
		
}
