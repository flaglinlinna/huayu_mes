package com.web.baseInfo.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 模具工序表
 *
 */
@Entity(name = "MjProcPrice")
@Table(name = MjProcPrice.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class MjProcPrice extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_MJ_PROC_PRICE";
	    
	    /**
	     * 工序名称
	     */
	    @ApiModelProperty(name = "procName", value = "工序名称")
	    @Column(length = 50)
	    protected String procName;

	    /**
	     * 工序费用
	     */
	    @ApiModelProperty(name = "fPrice", value = "工序费用")
	    @Column(length = 50)
	    protected String fPrice;

		/**
		 * 是否有效
		 */
		@ApiModelProperty(name = "enabled", value = "是否有效")
		@Column(length = 10)
		protected int enabled = 1;

		public String getProcName() {
			return procName;
		}

		public void setProcName(String procName) {
			this.procName = procName;
		}

		public String getfPrice() {
			return fPrice;
		}

		public void setfPrice(String fPrice) {
			this.fPrice = fPrice;
		}

		public int getEnabled() {
			return enabled;
		}

		public void setEnabled(int enabled) {
			this.enabled = enabled;
		}
}
