package com.web.quote.entity;

import com.app.base.entity.BaseEntity;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.entity.Unit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 报价BOM清单 导入临时表
 *
 */
@Entity(name = "QuoteBomTemp")
@Table(name = QuoteBomTemp.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteBomTemp extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "price_quote_bom_temp";

	/**
	 * 导入主表的id
	 */
	@ApiModelProperty(name="mid",value="导入主表的id")
	@Column
	protected Long mid;

	/**
	 * 错误信息
	 */
	@ApiModelProperty(name = "errorInfo", value = "错误信息")
	@Column(length = 1000)
	protected String errorInfo;

	/**
	 * 校验状态（0：校验通过 / 1：校验不通过 / 2：警告）
	 */
	@ApiModelProperty(name = "checkStatus", value = "校验状态（0：校验通过 / 1：校验不通过 / 2：警告）")
	@Column(length = 1)
	protected Integer checkStatus = 0;


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
     * 关联报价工作中心
     */
    @ApiModelProperty(name="pkBjWorkCenter",value="关联报价工作中心")
    @Column
    protected Long pkBjWorkCenter;
    
    @ApiModelProperty(name="wc",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkBjWorkCenter", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected BjWorkCenter wc;
    
    
    /**
     * 状态
     * 0：草稿，1:完成
     */
	@Column
	@ApiModelProperty(name="bsStatus",value="状态")
    protected int bsStatus = 0;

    
    /**
     * 关联物料类型
     */
    @ApiModelProperty(name="pkItemTypeWg",value="关联物料类型")
    @Column
    protected Long pkItemTypeWg;

	/**
	 * 关联物料类型
	 */
	@ApiModelProperty(name="itemType",value="关联物料类型")
	@Column
	protected String itemType;
    
    @ApiModelProperty(name="itp",hidden=true,value="物料类型")
    @ManyToOne
    @JoinColumn(name = "pkItemTypeWg", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected ItemTypeWg itp;

    /**
     * 关联单位
     */
    @ApiModelProperty(name="pkUnit",value="关联单位")
    @Column
    protected Long pkUnit;

	/**
	 * 采购单位
	 */
	@ApiModelProperty(name="purchaseUnit",value="采购单位")
	@Column
	protected String purchaseUnit;

	/**
	 * 关联单位名称 //上传时的名称
	 */
	@ApiModelProperty(name="pkUnit",value="关联单位")
	@Column
	protected String unitName;
    
    @ApiModelProperty(name="unit",hidden=true,value="单位")
    @ManyToOne
    @JoinColumn(name = "pkUnit", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Unit unit;

    /**
	 * 物料编码
	 */
	@ApiModelProperty(name = "bsItemCode", value = "物料编码")
	@Column(length = 50)
	protected String bsItemCode;

	/**
	 * 组件名称
	 */
	@ApiModelProperty(name = "bsElement", value = "组件名称")
	@Column(length = 150)
	protected String bsElement;
	
	/**
	 * 零件名称
	 */
	@ApiModelProperty(name = "bsComponent", value = "零件名称")
	@Column(length = 150)
	protected String bsComponent;
	

	/**
	 * 材料名称
	 */
	@ApiModelProperty(name = "bsMaterName", value = "材料名称")
	@Column(length = 200)
	protected String bsMaterName;

	/**
	 * 材料规格
	 */
	@ApiModelProperty(name = "bsModel", value = "材料规格")
	@Column(length = 200)
	protected String bsModel;

	/**
	 * 用量
	 */
	@ApiModelProperty(name = "bsQty", value = "用量")
	@Column(length = 50)
	protected String bsQty;

	/**
	 * 制品量
	 */
	@ApiModelProperty(name = "bsProQty", value = "制品量")
	@Column(length = 50)
	protected String bsProQty;

	/**
	 * 损耗分组
	 */
	@ApiModelProperty(name = "bsGroups", value = "bsGroups")
	@Column(length = 100)
	protected String bsGroups;
	
	/**
	 * 基数
	 */
	@ApiModelProperty(name = "bsRadix", value = "基数")
	@Column(length = 50)
	protected String bsRadix;

	/**
	 * 供应商
	 */
	@ApiModelProperty(name = "bsSupplier", value = "供应商")
	@Column(length = 50)
	protected String bsSupplier;

	/**
	 * 水口重 (g)
	 */
	@ApiModelProperty(name = "bsWaterGap", value = "水口量(注塑)")
	@Column(length = 150)
	protected String bsWaterGap;

	/**
	 * 穴数
	 */
	@ApiModelProperty(name = "bsCave", value = "穴数(注塑)")
	@Column(length = 150)
	protected String bsCave;

	/**
	 * 是否代采
	 * 0否 1是
	 */
	@ApiModelProperty(name = "bsAgent", value = "是否代采")
	@Column(length = 150)
	protected Integer bsAgent = 0;

	/**
	 * 是否单件
	 * 0否 1是
	 */
	@ApiModelProperty(name = "bsSingleton", value = "是否单件")
	@Column(length = 150)
	protected Integer bsSingleton = 0;

	/**
	 * 模内注塑
	 */
	@ApiModelProperty(name = "bsInjection", value = "模内注塑")
	@Column
	protected Integer bsInjection = 0;


	/**
	 * 采购说明
	 */
	@ApiModelProperty(name = "bsExplain", value = "采购说明")
	@Column(length = 200)
	protected String bsExplain;

	public String getBsElement() {
		return bsElement;
	}

	public void setBsElement(String bsElement) {
		this.bsElement = bsElement;
	}

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

	public Long getPkBjWorkCenter() {
		return pkBjWorkCenter;
	}

	public void setPkBjWorkCenter(Long pkBjWorkCenter) {
		this.pkBjWorkCenter = pkBjWorkCenter;
	}

	public BjWorkCenter getWc() {
		return wc;
	}

	public void setWc(BjWorkCenter wc) {
		this.wc = wc;
	}

	public Long getPkItemTypeWg() {
		return pkItemTypeWg;
	}

	public void setPkItemTypeWg(Long pkItemTypeWg) {
		this.pkItemTypeWg = pkItemTypeWg;
	}

	public ItemTypeWg getItp() {
		return itp;
	}

	public void setItp(ItemTypeWg itp) {
		this.itp = itp;
	}

	public Long getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(Long pkUnit) {
		this.pkUnit = pkUnit;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getBsItemCode() {
		return bsItemCode;
	}

	public void setBsItemCode(String bsItemCode) {
		this.bsItemCode = bsItemCode;
	}

	public String getBsComponent() {
		return bsComponent;
	}

	public void setBsComponent(String bsComponent) {
		this.bsComponent = bsComponent;
	}

	public String getBsMaterName() {
		return bsMaterName;
	}

	public void setBsMaterName(String bsMaterName) {
		this.bsMaterName = bsMaterName;
	}

	public String getBsModel() {
		return bsModel;
	}

	public void setBsModel(String bsModel) {
		this.bsModel = bsModel;
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getBsQty() {
		return bsQty;
	}

	public void setBsQty(String bsQty) {
		this.bsQty = bsQty;
	}

	public String getBsProQty() {
		return bsProQty;
	}

	public void setBsProQty(String bsProQty) {
		this.bsProQty = bsProQty;
	}

	public String getBsRadix() {
		return bsRadix;
	}

	public void setBsRadix(String bsRadix) {
		this.bsRadix = bsRadix;
	}

	public String getBsSupplier() {
		return bsSupplier;
	}

	public void setBsSupplier(String bsSupplier) {
		this.bsSupplier = bsSupplier;
	}

	public int getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(int bsStatus) {
		this.bsStatus = bsStatus;
	}

	public String getBsExplain() {
		return bsExplain;
	}

	public void setBsExplain(String bsExplain) {
		this.bsExplain = bsExplain;
	}

	public String getBsWaterGap() {
		return bsWaterGap;
	}

	public void setBsWaterGap(String bsWaterGap) {
		this.bsWaterGap = bsWaterGap;
	}

	public String getBsCave() {
		return bsCave;
	}

	public void setBsCave(String bsCave) {
		this.bsCave = bsCave;
	}

	public Integer getBsAgent() {
		return bsAgent;
	}

	public void setBsAgent(Integer bsAgent) {
		this.bsAgent = bsAgent;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public String getBsGroups() {
		return bsGroups;
	}

	public void setBsGroups(String bsGroups) {
		this.bsGroups = bsGroups;
	}

	public Integer getBsSingleton() {
		return bsSingleton;
	}

	public void setBsSingleton(Integer bsSingleton) {
		this.bsSingleton = bsSingleton;
	}

	public Integer getBsInjection() {
		return bsInjection;
	}

	public void setBsInjection(Integer bsInjection) {
		this.bsInjection = bsInjection;
	}
}
