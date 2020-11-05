package com.web.baseInfo.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工序基础信息表
 *
 */
@Entity(name = "Proc")
@Table(name = Proc.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Proc extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_base_PROC";

		/**
		 * 工作中心id
		 */
		@ApiModelProperty(name = "workcenterId", value = "工作中心id")
		@Column(length = 20)
		protected Long workcenterId;
	    
	    /**
	     * 工序编号
	     */
	    @ApiModelProperty(name = "procNo", value = "工序编号")
	    @Column(length = 50)
	    protected String procNo;

	    /**
	     * 工序名称
	     */
	    @ApiModelProperty(name = "procName", value = "工序名称")
	    @Column(length = 50)
	    protected String procName;

		/**
		 * 工序顺序号
		 */
		@ApiModelProperty(name = "procOrder", value = "工序顺序号")
		@Column(length = 50)
		protected String procOrder;

		/**
		 * 审核标识
		 */
		@ApiModelProperty(name = "checkStatus", value = "1")
		@Column(length = 50)
		protected int checkStatus = 1;


		public String getProcNo() {
			return procNo;
		}

		public void setProcNo(String procNo) {
			this.procNo = procNo;
		}

		public String getProcName() {
			return procName;
		}

		public void setProcName(String procName) {
			this.procName = procName;
		}

		public String getProcOrder() {
			return procOrder;
		}

		public void setProcOrder(String procOrder) {
			this.procOrder = procOrder;
		}

		public int getCheckStatus() {
			return checkStatus;
		}

		public void setCheckStatus(int checkStatus) {
			this.checkStatus = checkStatus;
		}

		public Long getWorkcenterId() {
			return workcenterId;
		}

		public void setWorkcenterId(Long workcenterId) {
			this.workcenterId = workcenterId;
		}
}
