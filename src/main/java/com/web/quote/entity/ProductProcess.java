package com.web.quote.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.web.basePrice.entity.BjModelType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.Transient;

import com.app.base.entity.BaseEntity;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.Proc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价工艺流程表
 *
 */
@Entity(name = "ProductProcess")
@Table(name = ProductProcess.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProductProcess extends BaseEntity {
	
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_product_process";
	
	/**
	 * 类型
	 * 五金:hardware
	 * 注塑:molding
	 * 表面处理:surface
	 * 组装:packag
	 * 外协:out
	 */
	@ApiModelProperty(name = "bsType", value = "类型")
	@Column(length = 50)
	protected String bsType;
	
	/**
     * 状态
     * 0：草稿，1:完成
     */
	@Column
	@ApiModelProperty(name="bsStatus",value="状态")
    protected int bsStatus = 0;
		
	/**
     * 是否需要填写
     * 0：需要，1:不需要
     */
	@Column
	@ApiModelProperty(name="bsNeed",value="是否需要填写")
    protected int bsNeed = 0;
	
	/**
     * 关联主表
     */
    @ApiModelProperty(name="pkQuote",value="报价主表")
    @Column
    protected Long pkQuote;

    @ApiModelProperty(name="quote",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkQuote", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Quote quote;
    
    /**
	 * Bom零件名称
	 */
	@ApiModelProperty(name = "bsName", value = "Bom零件名称")
	@Column(length = 500)
	protected String bsName;

	/**
	 * 关联零件
	 */
	@ApiModelProperty(name = "bsLinkName", value = "关联零件")
	@Column(length = 100)
	protected String bsLinkName;


	 /**
	 * 组件名称
	 */
	@ApiModelProperty(name = "bsElement", value = "组件名称")
	@Column(length = 500)
	protected String bsElement;

	/**
	 * 材料名称
	 */
	@ApiModelProperty(name = "bsMaterName", value = "材料名称")
	@Column(length = 500)
	protected String bsMaterName;

	/**
	 * 材料规格
	 */
	@ApiModelProperty(name = "bsModel", value = "材料规格")
	@Column(length = 500)
	protected String bsModel;
    
    /**
     * 关联工序表
     */
    @ApiModelProperty(name="pkProc",value="工序表")
    @Column
    protected Long pkProc;
    
    @ApiModelProperty(name="proc",hidden=true,value="工序表")
    @ManyToOne
    @JoinColumn(name = "pkProc", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Proc proc;
    
	
	/**
	 * 工序顺序
	 */
	@ApiModelProperty(name = "bsOrder", value = "工序顺序")
	protected int bsOrder;
	
	/**
	 * 机台类型编码
	 */
	@ApiModelProperty(name = "bsModelType", value = "机台类型编码")
	@Column(length = 100)
	protected String bsModelType;

	@ApiModelProperty(name="bjModelType",hidden=true,value="机台类型")
	@ManyToOne
	@JoinColumn(name = "bsModelType",referencedColumnName = "modelCode", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	protected BjModelType bjModelType;

	/**
	 * 基数
	 */
	@ApiModelProperty(name = "bsRadix", value = "基数")
	@Column(length = 50)
	protected BigDecimal bsRadix= new BigDecimal("1");
	
	/**
	 * 人数
	 */
	@ApiModelProperty(name = "bsUserNum", value = "人数")
	@Column(length = 50)
	protected BigDecimal bsUserNum;
	
	/**
	 * 成型周期
	 */
	@ApiModelProperty(name = "bsCycle", value = "成型周期")
	@Column(length = 50)
	protected BigDecimal bsCycle;
	
	/**
	 * 工序良率(%)
	 */
	@ApiModelProperty(name = "bsYield", value = "工序良率(%)")
	@Column(length = 50)
	protected BigDecimal bsYield;
	
	
	/**
	 * 本工序损耗-人工
	 * 总费用*100/工序良率-总费用
	 */
	@ApiModelProperty(name = "bsLossTheLh", value = "本工序损耗")
	@Column(length = 100)
	protected BigDecimal bsLossTheLh;
	/**
	 * 本工序损耗-制费
	 * 总费用*100/工序良率-总费用
	 */
	@ApiModelProperty(name = "bsLossTheMh", value = "本工序损耗")
	@Column(length = 100)
	protected BigDecimal bsLossTheMh;
	
	/**
	 * 后工序良率(%)
	 */
	@ApiModelProperty(name = "bsHouYield", value = "后工序良率(%)")
	@Column(length = 50)
	protected BigDecimal bsHouYield;

	/**
	 * 人工-后工序损耗
	 * 总费用*100/工序良率-总费用
	 */
	@ApiModelProperty(name = "bsLossHouLh", value = "后工序损耗")
	@Column(length = 100)
	protected BigDecimal bsLossHouLh;
	/**
	 * 制费-后工序损耗
	 * 总费用*100/工序良率-总费用
	 */
	@ApiModelProperty(name = "bsLossHouMh", value = "后工序损耗")
	@Column(length = 100)
	protected BigDecimal bsLossHouMh;
	
	/**
	 * 穴数
	 */
	@ApiModelProperty(name = "bsCave", value = "穴数")
	@Column(length = 100)
	protected String bsCave;

	/**
	 * 产能
	 */
	@ApiModelProperty(name = "bsCapacity", value = "产能")
	@Column(length = 100)
	protected String bsCapacity;
	
	/**
	 * 人工费率（元/小时）
	 */
	@ApiModelProperty(name = "feeLh", value = "人工费率（元/小时）")
	@Column(length = 50)
	protected BigDecimal bsFeeLh;

	/**
	 * 制费费率（元/小时）
	 */
	@ApiModelProperty(name = "feeMh", value = "制费费率（元/小时）")
	@Column(length = 50)
	protected BigDecimal bsFeeMh;
	
	/**
	 * 人工费率总
	 */
	@ApiModelProperty(name = "bsFeeLhAll", value = "人工费率总")
	@Column(length = 50)
	protected BigDecimal bsFeeLhAll;

	/**
	 * 制费费率总
	 */
	@ApiModelProperty(name = "bsFeeMhAll", value = "制费费率总")
	@Column(length = 50)
	protected BigDecimal bsFeeMhAll;
	
	/**
	 * 外协价格
	 */
	@ApiModelProperty(name = "bsFeeWxAll", value = "外协价格")
	@Column(length = 50)
	protected BigDecimal bsFeeWxAll;

	/**
	 * 损耗率
	 */
	@ApiModelProperty(name = "bsLoss", value = "损耗率")
	@Column(length = 50)
	protected BigDecimal bsLoss;
	
	@Transient
    private String bsTypeList;

	/**
	 * 附件id
	 */
	@ApiModelProperty(name = "fileId", value = "附件id")
	protected Long fileId;

	/**
	 * 附件名称
	 */
	@ApiModelProperty(name = "fileName", value = "附件名称")
	@Column(length = 100)
	protected String fileName;

	/**
	 * 复制的bomId
	 */
	@ApiModelProperty(name = "pkBomId", value = "复制的bomId")
	@Column(length = 20)
	protected Long pkBomId;

//	/**
//	 * 工艺ID ，避免重复下发及修改
//	 */
//	@ApiModelProperty(name = "pkQuoteProcessId", value = "避免重复下发及修改")
//	@Column(length = 20)
//	protected Long pkQuoteProcessId;

	/**
	 * * 2021-03-18 hjj 修改损耗计算方式
	 * 成本 bsCost
	 */
	@ApiModelProperty(name = "bsCost", value = "成本")
	@Column( precision=21, scale=5)
	protected BigDecimal bsCost;

	@ApiModelProperty(name = "bsMaterCost", value = "材料成本")
	@Column( precision=21, scale=5)
	protected BigDecimal bsMaterCost;

	@ApiModelProperty(name = "bsTheLoss", value = "本工序损耗")
	@Column( precision=21, scale=5)
	protected BigDecimal bsTheLoss;

	@ApiModelProperty(name = "bsAllLoss", value = "成本累计(含损耗)")
	@Column( precision=21, scale=5)
	protected BigDecimal bsAllLoss;

	@ApiModelProperty(name = "bsFreight", value = "包装运输费")
	@Column( precision=21, scale=5)
	protected BigDecimal bsFreight=BigDecimal.ZERO;

	/**
	 * * 2021-04-14 hjj 增加损耗分组计算
	 * bsGroups分组名称
	 */
	/**
	 * 分组名称
	 */
	@ApiModelProperty(name = "bsGroups", value = "分组名称")
	@Column(length = 100)
	protected String bsGroups;

	/**
	 * 复制源ID
	 */
	@ApiModelProperty(name="copyId",value="复制源ID")
	@Column
	protected Long copyId;

	/**
	 * 单件
	 */
	@ApiModelProperty(name = "bsSingleton", value = "单件")
	@Column
	protected Integer bsSingleton = 0;

	/**
	 * 采购单位
	 */
	@ApiModelProperty(name = "purchaseUnit", value = "采购单位")
	@Column(length = 100)
	protected String purchaseUnit;

	public Long getPkQuote() {
		return pkQuote;
	}

	public void setPkQuote(Long pkQuote) {
		this.pkQuote = pkQuote;
	}

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public Long getPkProc() {
		return pkProc;
	}

	public void setPkProc(Long pkProc) {
		this.pkProc = pkProc;
	}

	public Proc getProc() {
		return proc;
	}

	public void setProc(Proc proc) {
		this.proc = proc;
	}

	

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public int getBsOrder() {
		return bsOrder;
	}

	public void setBsOrder(int bsOrder) {
		this.bsOrder = bsOrder;
	}

	public int getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(int bsStatus) {
		this.bsStatus = bsStatus;
	}

	public int getBsNeed() {
		return bsNeed;
	}

	public void setBsNeed(int bsNeed) {
		this.bsNeed = bsNeed;
	}

	public String getBsType() {
		return bsType;
	}

	public void setBsType(String bsType) {
		this.bsType = bsType;
	}

	public String getBsModelType() {
		return bsModelType;
	}

	public void setBsModelType(String bsModelType) {
		this.bsModelType = bsModelType;
	}

	public BigDecimal getBsRadix() {
		return bsRadix;
	}

	public void setBsRadix(BigDecimal bsRadix) {
		this.bsRadix = bsRadix;
	}

	public BigDecimal getBsUserNum() {
		return bsUserNum;
	}

	public void setBsUserNum(BigDecimal bsUserNum) {
		this.bsUserNum = bsUserNum;
	}

	public BigDecimal getBsCycle() {
		return bsCycle;
	}

	public void setBsCycle(BigDecimal bsCycle) {
		this.bsCycle = bsCycle;
	}

	public BigDecimal getBsYield() {
		return bsYield;
	}

	public void setBsYield(BigDecimal bsYield) {
		this.bsYield = bsYield;
	}

	public String getBsCave() {
		return bsCave;
	}

	public void setBsCave(String bsCave) {
		this.bsCave = bsCave;
	}

	public String getBsCapacity() {
		return bsCapacity;
	}

	public void setBsCapacity(String bsCapacity) {
		this.bsCapacity = bsCapacity;
	}

	public BigDecimal getBsFeeLh() {
		return bsFeeLh;
	}

	public void setBsFeeLh(BigDecimal bsFeeLh) {
		this.bsFeeLh = bsFeeLh;
	}

	public BigDecimal getBsFeeMh() {
		return bsFeeMh;
	}

	public void setBsFeeMh(BigDecimal bsFeeMh) {
		this.bsFeeMh = bsFeeMh;
	}

	public BigDecimal getBsFeeLhAll() {
		return bsFeeLhAll;
	}

	public void setBsFeeLhAll(BigDecimal bsFeeLhAll) {
		this.bsFeeLhAll = bsFeeLhAll;
	}

	public BigDecimal getBsFeeMhAll() {
		return bsFeeMhAll;
	}

	public void setBsFeeMhAll(BigDecimal bsFeeMhAll) {
		this.bsFeeMhAll = bsFeeMhAll;
	}

	public BigDecimal getBsFeeWxAll() {
		return bsFeeWxAll;
	}

	public void setBsFeeWxAll(BigDecimal bsFeeWxAll) {
		this.bsFeeWxAll = bsFeeWxAll;
	}

	public BigDecimal getBsHouYield() {
		return bsHouYield;
	}

	public void setBsHouYield(BigDecimal bsHouYield) {
		this.bsHouYield = bsHouYield;
	}

	public BigDecimal getBsLoss() {
		return bsLoss;
	}

	public void setBsLoss(BigDecimal bsLoss) {
		this.bsLoss = bsLoss;
	}

	public BigDecimal getBsLossTheLh() {
		return bsLossTheLh;
	}

	public void setBsLossTheLh(BigDecimal bsLossTheLh) {
		this.bsLossTheLh = bsLossTheLh;
	}

	public BigDecimal getBsLossHouLh() {
		return bsLossHouLh;
	}

	public void setBsLossHouLh(BigDecimal bsLossHouLh) {
		this.bsLossHouLh = bsLossHouLh;
	}

	public BigDecimal getBsLossTheMh() {
		return bsLossTheMh;
	}

	public void setBsLossTheMh(BigDecimal bsLossTheMh) {
		this.bsLossTheMh = bsLossTheMh;
	}

	public BigDecimal getBsLossHouMh() {
		return bsLossHouMh;
	}

	public void setBsLossHouMh(BigDecimal bsLossHouMh) {
		this.bsLossHouMh = bsLossHouMh;
	}

	public String getBsElement() {
		return bsElement;
	}

	public void setBsElement(String bsElement) {
		this.bsElement = bsElement;
	}

	public String getBsTypeList() {
		return bsTypeList;
	}

	public void setBsTypeList(String bsTypeList) {
		this.bsTypeList = bsTypeList;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getPkBomId() {
		return pkBomId;
	}

	public void setPkBomId(Long pkBomId) {
		this.pkBomId = pkBomId;
	}

	public BigDecimal getBsCost() {
		return bsCost;
	}

	public void setBsCost(BigDecimal bsCost) {
		this.bsCost = bsCost;
	}

	public BigDecimal getBsTheLoss() {
		return bsTheLoss;
	}

	public void setBsTheLoss(BigDecimal bsTheLoss) {
		this.bsTheLoss = bsTheLoss;
	}

	public BigDecimal getBsAllLoss() {
		return bsAllLoss;
	}

	public void setBsAllLoss(BigDecimal bsAllLoss) {
		this.bsAllLoss = bsAllLoss;
	}

	public BigDecimal getBsMaterCost() {
		return bsMaterCost;
	}

	public void setBsMaterCost(BigDecimal bsMaterCost) {
		this.bsMaterCost = bsMaterCost;
	}

	public String getBsMaterName() {
		return bsMaterName;
	}

	public void setBsMaterName(String bsMaterName) {
		this.bsMaterName = bsMaterName;
	}

	public String getBsGroups() {
		return bsGroups;
	}

	public void setBsGroups(String bsGroups) {
		this.bsGroups = bsGroups;
	}

	public BjModelType getBjModelType() {
		return bjModelType;
	}

	public void setBjModelType(BjModelType bjModelType) {
		this.bjModelType = bjModelType;
	}

	public String getBsLinkName() {
		return bsLinkName;
	}

	public void setBsLinkName(String bsLinkName) {
		this.bsLinkName = bsLinkName;
	}

	public BigDecimal getBsFreight() {
		return bsFreight;
	}

	public void setBsFreight(BigDecimal bsFreight) {
		this.bsFreight = bsFreight;
	}

	public Long getCopyId() {
		return copyId;
	}

	public void setCopyId(Long copyId) {
		this.copyId = copyId;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public Integer getBsSingleton() {
		return bsSingleton;
	}

	public void setBsSingleton(Integer bsSingleton) {
		this.bsSingleton = bsSingleton;
	}

	public String getBsModel() {
		return bsModel;
	}

	public void setBsModel(String bsModel) {
		this.bsModel = bsModel;
	}
}
