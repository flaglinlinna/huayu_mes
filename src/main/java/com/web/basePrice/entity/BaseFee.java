package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 人工制费维护
 *
 */
@Entity(name = "BaseFee")
@Table(name = BaseFee.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class BaseFee extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_FEE";
	    
	    /**
	     * 工作中心Id
	     */
	    @ApiModelProperty(name = "workcenterId", value = "工作中心Id")
	    @Column(length = 20)
	    protected Long workcenterId;

		/**
		 * 是否有效
		 */
		@ApiModelProperty(name = "enabled", value = "是否有效")
		@Column(length = 10)
		protected int enabled = 1;

	    /**
	     * 工序名称
	     */
	    @ApiModelProperty(name = "procName", value = "工序名称")
	    @Column(length = 50)
	    protected String procName;

		/**
		 * 机台类型
		 */
		@ApiModelProperty(name = "mhType", value = "机台类型")
		@Column(length = 20)
		protected String mhType;

		/**
		 * 人工费率（元/小时）
		 */
		@ApiModelProperty(name = "feeLh", value = "人工费率（元/小时）")
		@Column(length = 30)
		protected String feeLh;

		/**
		 * 制费费率（元/小时）
		 */
		@ApiModelProperty(name = "feeMh", value = "制费费率（元/小时）")
		@Column(length = 10)
		protected String feeMh;


		public Long getWorkcenterId() {
			return workcenterId;
		}

		public void setWorkcenterId(Long workcenterId) {
			this.workcenterId = workcenterId;
		}

		public int getEnabled() {
			return enabled;
		}

		public void setEnabled(int enabled) {
			this.enabled = enabled;
		}

		public String getProcName() {
			return procName;
		}

		public void setProcName(String procName) {
			this.procName = procName;
		}

		public String getMhType() {
			return mhType;
		}

		public void setMhType(String mhType) {
			this.mhType = mhType;
		}

		public String getFeeLh() {
			return feeLh;
		}

		public void setFeeLh(String feeLh) {
			this.feeLh = feeLh;
		}

		public String getFeeMh() {
			return feeMh;
		}

		public void setFeeMh(String feeMh) {
			this.feeMh = feeMh;
		}
}
