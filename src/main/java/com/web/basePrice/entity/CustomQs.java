package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 客户品质标准
 *
 */
@Entity(name = "CustomQs")
@Table(name = CustomQs.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class CustomQs extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_CUSTOM_QS";
	    
	    /**
	     * 客户名称
	     */
	    @ApiModelProperty(name = "custName", value = "客户名称")
	    @Column(length = 50)
	    protected String custName;

	    /**
	     * 品质标准编号
	     */
	    @ApiModelProperty(name = "qsNo", value = "品质标准编号")
	    @Column(length = 50)
	    protected String qsNo;

		/**
		 * 品质标准名称
		 */
		@ApiModelProperty(name = "qsName", value = "品质标准名称")
		@Column(length = 50)
		protected String qsName;

		/**
		 * 品质标准类型
		 */
		@ApiModelProperty(name = "qsType", value = "品质标准类型")
		@Column(length = 50)
		protected String qsType;

		/**
		 * 品质标准附件
		 */
		@ApiModelProperty(name = "fftp", value = "品质标准附件")
		@Column(length = 200)
		protected String fftp;

		/**
		 * 附件id
		 */
		@ApiModelProperty(name = "fileId", value = "附件id")
		@Column(length = 200)
		protected String fileId;

		public String getCustName() {
			return custName;
		}

		public void setCustName(String custName) {
			this.custName = custName;
		}

		public String getQsNo() {
			return qsNo;
		}

		public void setQsNo(String qsNo) {
			this.qsNo = qsNo;
		}

		public String getQsName() {
			return qsName;
		}

		public void setQsName(String qsName) {
			this.qsName = qsName;
		}

		public String getQsType() {
			return qsType;
		}

		public void setQsType(String qsType) {
			this.qsType = qsType;
		}

		public String getFftp() {
			return fftp;
		}

		public void setFftp(String fftp) {
			this.fftp = fftp;
		}

		public String getFileId() {
			return fileId;
		}

		public void setFileId(String fileId) {
			this.fileId = fileId;
		}
}
