package com.web.quote.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basePrice.entity.ProfitProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价信息主表
 *
 */
@Entity(name = "Quote")
@Table(name = Quote.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Quote extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_quote";
	
	/**
     * 状态
     * 0：草稿，1:
     * 99:关闭
     */
	@Column
	@NotNull
	@ApiModelProperty(name="bsStatus",value="状态")
    protected int bsStatus = 0;

	//---业务字段
	/**
	 * 报价单编号
	 */
	@ApiModelProperty(name = "bsCode", value = "报价单编号")
	@Column(length = 100)
	protected String bsCode;
	
	/**
	 * 报价类型
	 */
	@ApiModelProperty(name = "bsType", value = "报价类型")
	@Column(length = 100)
	protected String bsType;
	
	/**
	 * 完成日期
	 */
	@ApiModelProperty(name="bsFinishTime",value="完成日期")
	@Column(length = 50)
	protected String bsFinishTime;
	
	/**
	 * 项目编号
	 */
	@ApiModelProperty(name = "bsProject", value = "项目编号")
	@Column(length = 150)
	protected String bsProject;
	
	/**
	 * 项目版本
	 */
	@ApiModelProperty(name = "bsProjVer", value = "项目版本")
	@Column(length = 100)
	protected String bsProjVer;
	
	/**
	 * 报价备注
	 */
	@ApiModelProperty(name = "bsRemarks", value = "报价备注")
	@Column(length = 300)
	protected String bsRemarks;
	
	/**
	 * 产品型号
	 */
	@ApiModelProperty(name = "bsProd", value = "产品型号")
	@Column(length = 150)
	protected String bsProd;
	
	/**
	 * 相似产品
	 */
	@ApiModelProperty(name = "bsSimilarProd", value = "相似产品")
	@Column(length = 150)
	protected String bsSimilarProd;
	
	/**
	 * 机种型号
	 */
	@ApiModelProperty(name = "bsDevType", value = "机种型号")
	@Column(length = 100)
	protected String bsDevType;
	
	/**
	 * 产品类型
	 */
	@ApiModelProperty(name = "bsProdType", value = "产品类型")
	@Column(length = 50)
	protected String bsProdType;
	
	/**
	 * 关联产品利润率维护表
	 * **/	
	@ApiModelProperty(name="pkProfitProd",value="产品利润率表ID")
    @Column
    protected Long pkProfitProd;

    @ApiModelProperty(name="profitProd",hidden=true,value="产品利润率表")
    @ManyToOne
    @JoinColumn(name = "pkProfitProd", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected ProfitProd profitProd;
	
	/**
	 * 客户名称
	 */
	@ApiModelProperty(name = "bsCustName", value = "客户名称")
	@Column(length = 150)
	protected String bsCustName;
	
	/**
	 * 市场定位
	 */
	@ApiModelProperty(name = "bsPosition", value = "市场定位")
	@Column(length = 100)
	protected String bsPosition;
	
	/**
	 * 客户提供资料
	 */
	@ApiModelProperty(name = "bsMaterial", value = "客户提供资料")
	@Column(length = 200)
	protected String bsMaterial;
	
	/**
	 * 外观检验项【多选】
	 */
	@ApiModelProperty(name = "bsChkOutItem", value = "外观检验项【多选】")
	@Column(length = 100)
	protected String bsChkOutItem;
	
	/**
	 * 外观检验
	 */
	@ApiModelProperty(name = "bsChkOut", value = "外观检验")
	@Column(length = 200)
	protected String bsChkOut;
	
	/**
	 * 功能性能项【多选】
	 */
	@ApiModelProperty(name = "bsFunctionItem", value = "功能性能项【多选】")
	@Column(length = 100)
	protected String bsFunctionItem;
	
	/**
	 * 功能性能
	 */
	@ApiModelProperty(name = "bsFunction", value = "功能性能")
	@Column(length = 200)
	protected String bsFunction;
	
	/**
	 * 环保要求
	 */
	@ApiModelProperty(name = "bsRequire", value = "环保要求")
	@Column(length = 200)
	protected String bsRequire;
	
	/**
	 * 防水防尘等级
	 */
	@ApiModelProperty(name = "bsLevel", value = "防水防尘等级")
	@Column(length = 50)
	protected String bsLevel;
	
	/**
	 * 客户其他要求
	 */
	@ApiModelProperty(name = "bsCustRequire", value = "客户其他要求")
	@Column(length = 500)
	protected String bsCustRequire;
	
	/**
	 * 业务承办人
	 */
	@ApiModelProperty(name = "bsUndertaker", value = "业务承办人")
	@Column(length = 100)
	protected String bsUndertaker;
	
	/**
	 * 核准人
	 */
	@ApiModelProperty(name = "bsApproved", value = "核准人")
	@Column(length = 100)
	protected String bsApproved;
	
	/**
     * 核准时间
     */
	
	@ApiModelProperty(name="bsApprovalTime",value="核准时间")
	@Column(length = 50)
	protected String  bsApprovalTime;
	

	public String getBsCode() {
		return bsCode;
	}

	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}

	public String getBsType() {
		return bsType;
	}

	public void setBsType(String bsType) {
		this.bsType = bsType;
	}

	public int getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(int bsStatus) {
		this.bsStatus = bsStatus;
	}

	public String getBsProject() {
		return bsProject;
	}

	public void setBsProject(String bsProject) {
		this.bsProject = bsProject;
	}

	public String getBsProjVer() {
		return bsProjVer;
	}

	public void setBsProjVer(String bsProjVer) {
		this.bsProjVer = bsProjVer;
	}

	public String getBsRemarks() {
		return bsRemarks;
	}

	public void setBsRemarks(String bsRemarks) {
		this.bsRemarks = bsRemarks;
	}

	public String getBsProd() {
		return bsProd;
	}

	public void setBsProd(String bsProd) {
		this.bsProd = bsProd;
	}

	public String getBsSimilarProd() {
		return bsSimilarProd;
	}

	public void setBsSimilarProd(String bsSimilarProd) {
		this.bsSimilarProd = bsSimilarProd;
	}

	public String getBsDevType() {
		return bsDevType;
	}

	public void setBsDevType(String bsDevType) {
		this.bsDevType = bsDevType;
	}

	public String getBsProdType() {
		return bsProdType;
	}

	public void setBsProdType(String bsProdType) {
		this.bsProdType = bsProdType;
	}

	public String getBsCustName() {
		return bsCustName;
	}

	public void setBsCustName(String bsCustName) {
		this.bsCustName = bsCustName;
	}

	public String getBsPosition() {
		return bsPosition;
	}

	public void setBsPosition(String bsPosition) {
		this.bsPosition = bsPosition;
	}

	public String getBsMaterial() {
		return bsMaterial;
	}

	public void setBsMaterial(String bsMaterial) {
		this.bsMaterial = bsMaterial;
	}

	public String getBsChkOut() {
		return bsChkOut;
	}

	public void setBsChkOut(String bsChkOut) {
		this.bsChkOut = bsChkOut;
	}

	public String getBsFunction() {
		return bsFunction;
	}

	public void setBsFunction(String bsFunction) {
		this.bsFunction = bsFunction;
	}

	public String getBsRequire() {
		return bsRequire;
	}

	public void setBsRequire(String bsRequire) {
		this.bsRequire = bsRequire;
	}

	public String getBsLevel() {
		return bsLevel;
	}

	public void setBsLevel(String bsLevel) {
		this.bsLevel = bsLevel;
	}

	public String getBsCustRequire() {
		return bsCustRequire;
	}

	public void setBsCustRequire(String bsCustRequire) {
		this.bsCustRequire = bsCustRequire;
	}

	public String getBsUndertaker() {
		return bsUndertaker;
	}

	public void setBsUndertaker(String bsUndertaker) {
		this.bsUndertaker = bsUndertaker;
	}

	public String getBsApproved() {
		return bsApproved;
	}

	public void setBsApproved(String bsApproved) {
		this.bsApproved = bsApproved;
	}

	public Long getPkProfitProd() {
		return pkProfitProd;
	}

	public void setPkProfitProd(Long pkProfitProd) {
		this.pkProfitProd = pkProfitProd;
	}

	public ProfitProd getProfitProd() {
		return profitProd;
	}

	public void setProfitProd(ProfitProd profitProd) {
		this.profitProd = profitProd;
	}

	public String getBsChkOutItem() {
		return bsChkOutItem;
	}

	public void setBsChkOutItem(String bsChkOutItem) {
		this.bsChkOutItem = bsChkOutItem;
	}

	public String getBsFunctionItem() {
		return bsFunctionItem;
	}

	public void setBsFunctionItem(String bsFunctionItem) {
		this.bsFunctionItem = bsFunctionItem;
	}

	public String getBsFinishTime() {
		return bsFinishTime;
	}

	public void setBsFinishTime(String bsFinishTime) {
		this.bsFinishTime = bsFinishTime;
	}

	public String getBsApprovalTime() {
		return bsApprovalTime;
	}

	public void setBsApprovalTime(String bsApprovalTime) {
		this.bsApprovalTime = bsApprovalTime;
	}
}
