package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 模具成本导入-主表
 *
 */
@Entity(name = "MjProcFee")
@Table(name = MjProcFee.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class MjProcFee extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_MJ_PROC_FEE";
	    
		/**
	     * 产品类型ID
	     */
	    @ApiModelProperty(name = "productTypeId", value = "产品类型ID")
	    @Column(length = 20)
	    protected Long productTypeId;
	    
	    /**
	     * 产品编号
	     */
	    @ApiModelProperty(name = "productCode", value = "产品编号")
	    @Column(length = 200)
	    protected String productCode;

	    /**
	     * 图示
	     */
	    @ApiModelProperty(name = "fimg", value = "图示")
	    @Column(length = 500)
	    protected String fimg;

		/**
		 * 产品名称
		 */
		@ApiModelProperty(name = "productName", value = "产品名称")
		@Column(length = 50)
		protected String productName;

		/**
		 * 模具结构
		 */
		@ApiModelProperty(name = "structureMj", value = "模具结构")
		@Column(length = 50)
		protected String structureMj;
		
		/**
		 * 模具价格
		 */
		@ApiModelProperty(name = "mjPrice", value = "")
		@Column(length = 20)
		protected BigDecimal mjPrice;

		/**
		 * 穴数
		 */
		@ApiModelProperty(name = "numHole", value = "穴数")
		@Column(length = 20)
		protected BigDecimal numHole;

		/**
		 * 工序费用（元/小时）
		 */
		@ApiModelProperty(name = "feeProc", value = "工序费用（元/小时）")
		@Column(length = 20)
		protected BigDecimal feeProc;

		/**
		 * 钢料+配件+热处理费用
		 */
		@ApiModelProperty(name = "feeType1", value = "钢料+配件+热处理费用")
		@Column(length = 20)
		protected BigDecimal feeType1;

		/**
		 * 铜公材料费
		 */
		@ApiModelProperty(name = "feeType2", value = "铜公材料费")
		@Column(length = 20)
		protected BigDecimal feeType2;

		/**
		 * 模胚价格+加工费用
		 */
		@ApiModelProperty(name = "feeType3", value = "模胚价格+加工费用")
		@Column(length = 20)
		protected BigDecimal feeType3;

		/**
		 * 热流通费用
		 */
		@ApiModelProperty(name = "feeType4", value = "热流通费用")
		@Column(length = 20)
		protected BigDecimal feeType4;

		/**
		 * 费用(预留)
		 */
		@ApiModelProperty(name = "feeType5", value = "费用(预留)")
		@Column(length = 20)
		protected BigDecimal feeType5;

		/**
		 * 参考报价
		 */
		@ApiModelProperty(name = "stQuote", value = "参考报价")
		@Column(length = 20)
		protected BigDecimal stQuote;
		
		/**
		 * 评估总费用（含税）
		 */
		@ApiModelProperty(name = "feeAll", value = "评估总费用（含税）")
		@Column(length = 20)
		protected BigDecimal feeAll;

		/**
		 * 附件id
		 */
		@ApiModelProperty(name = "fileId", value = "附件id")
		@Column(length = 200)
		protected String fileId;

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getFimg() {
		return fimg;
	}

	public void setFimg(String fimg) {
		this.fimg = fimg;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStructureMj() {
		return structureMj;
	}

	public void setStructureMj(String structureMj) {
		this.structureMj = structureMj;
	}

	public BigDecimal getNumHole() {
		return numHole;
	}

	public void setNumHole(BigDecimal numHole) {
		this.numHole = numHole;
	}

	public BigDecimal getFeeProc() {
		return feeProc;
	}

	public void setFeeProc(BigDecimal feeProc) {
		this.feeProc = feeProc;
	}

	public BigDecimal getFeeType1() {
		return feeType1;
	}

	public void setFeeType1(BigDecimal feeType1) {
		this.feeType1 = feeType1;
	}

	public BigDecimal getFeeType2() {
		return feeType2;
	}

	public void setFeeType2(BigDecimal feeType2) {
		this.feeType2 = feeType2;
	}

	public BigDecimal getFeeType3() {
		return feeType3;
	}

	public void setFeeType3(BigDecimal feeType3) {
		this.feeType3 = feeType3;
	}

	public BigDecimal getFeeType4() {
		return feeType4;
	}

	public void setFeeType4(BigDecimal feeType4) {
		this.feeType4 = feeType4;
	}

	public BigDecimal getFeeType5() {
		return feeType5;
	}

	public void setFeeType5(BigDecimal feeType5) {
		this.feeType5 = feeType5;
	}

	public BigDecimal getFeeAll() {
		return feeAll;
	}

	public void setFeeAll(BigDecimal feeAll) {
		this.feeAll = feeAll;
	}

	public BigDecimal getMjPrice() {
		return mjPrice;
	}

	public void setMjPrice(BigDecimal mjPrice) {
		this.mjPrice = mjPrice;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public BigDecimal getStQuote() {
		return stQuote;
	}

	public void setStQuote(BigDecimal stQuote) {
		this.stQuote = stQuote;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
