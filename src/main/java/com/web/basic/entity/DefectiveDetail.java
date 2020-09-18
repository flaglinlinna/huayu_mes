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
	    public static final String TABLE_NAME = "basic_chk_bad_det";
	    
	    
	    /**
	      * 关联不良类别的ID
	      */
	     @ApiModelProperty(name="pkDefective",value="不良类别ID")
	     @Column
	     protected Long pkDefective;

	     @ApiModelProperty(name="defective",hidden=true,value="不良类别ID")
	     @ManyToOne
	     @JoinColumn(name = "pkDefective", insertable = false, updatable = false)
	     @NotFound(action = NotFoundAction.IGNORE)
	     protected Defective defective;
	    
	    
	    /**
	     * 不良 编码
	     */
	    @ApiModelProperty(name = "bsCode", value = "不良 编码")
	    @Column(length = 50)
	    protected String bsCode;

	    /**
	     * 不良 名称
	     */
	    @ApiModelProperty(name = "bsName", value = "不良 名称")
	    @Column(length = 50)
	    protected String bsName;
	    
	    /**
	     * 状态（0：正常 / 1：禁用）
	     */
	    @ApiModelProperty(name = "bsStatus", value = "状态（0：正常 / 1：禁用）")
	    @Column
	    protected Integer bsStatus = 0;
	    
		

		public Long getPkDefective() {
			return pkDefective;
		}

		public void setPkDefective(Long pkDefective) {
			this.pkDefective = pkDefective;
		}

		public Defective getDefective() {
			return defective;
		}

		public void setDefective(Defective defective) {
			this.defective = defective;
		}

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
