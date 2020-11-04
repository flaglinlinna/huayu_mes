package com.web.baseInfo.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工作中心维护表
 *
 */
@Entity(name = "BjWorkCenter")
@Table(name = BjWorkCenter.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class BjWorkCenter extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_base_WORKCENTER";
	    
	    /**
	     * 工作中心编号
	     */
	    @ApiModelProperty(name = "workcenterCode", value = "工作中心编号")
	    @Column(length = 50)
	    protected String workcenterCode;

	    /**
	     * 工作中心名称
	     */
	    @ApiModelProperty(name = "workcenterName", value = "工作中心名称")
	    @Column(length = 50)
	    protected String workcenterName;

		/**
		 * 单位名称
		 */
		@ApiModelProperty(name = "unitName", value = "单位名称")
		@Column(length = 50)
		protected int checkStatus = 1;

	public String getWorkcenterCode() {
		return workcenterCode;
	}

	public void setWorkcenterCode(String workcenterCode) {
		this.workcenterCode = workcenterCode;
	}

	public String getWorkcenterName() {
		return workcenterName;
	}

	public void setWorkcenterName(String workcenterName) {
		this.workcenterName = workcenterName;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
}
