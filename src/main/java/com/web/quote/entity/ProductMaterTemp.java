package com.web.quote.entity;

import com.app.base.entity.BaseEntity;
import com.web.basePrice.entity.Unit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 制作部材料信息表  导入临时表
 *
 */
@Entity(name = "ProductMaterTemp")
@Table(name = ProductMaterTemp.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProductMaterTemp extends BaseEntity {
	private static final long serialVersionUID = 4625660587007894370L;

	public static final String TABLE_NAME = "PRICE_PRODUCT_MATER_TEMP";
	
	/**
	 * 类型
	 * 五金:hardware
	 * 注塑:molding
	 * 表面处理:surface
	 * 组装:packag
	 */
	@ApiModelProperty(name = "bsType", value = "类型")
	@Column(length = 50)
	protected String bsType;


	/**
	 * 临时表判断是否为采购单填写导入 0是 1否
	 */
	@ApiModelProperty(name = "bsPurchase", value = "1")
	@Column
	protected Integer bsPurchase;

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


	/**
	 * 主表id(导入的主表)
	 */
	@ApiModelProperty(name = "mid", value = "导入的主表")
	@Column(length = 20)
	protected Long mid;

	/**
	 * 零件名称
	 */
	@ApiModelProperty(name = "bsComponent", value = "零件名称")
	@Column(length = 200)
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
	@Column(length = 150)
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
	 * 单位
	 */
	@ApiModelProperty(name = "unit", value = "单位")
	@Column(length = 50)
	protected String bsUnit;
	
	/**
     * 关联单位
     */
    @ApiModelProperty(name="pkUnit",value="关联单位")
    @Column
    protected Long pkUnit;
    
    @ApiModelProperty(name="unit",hidden=true,value="单位")
    @ManyToOne
    @JoinColumn(name = "pkUnit", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Unit unit;

	/**
	 * 基数
	 */
	@ApiModelProperty(name = "bsRadix", value = "基数")
	@Column(length = 50)
	protected String bsRadix;

	/**
	 * 供应商
	 */
	@ApiModelProperty(name = "supplier", value = "供应商")
	@Column(length = 200)
	protected String bsSupplier;

	/**
	 * 水口量(注塑)
	 */
	@ApiModelProperty(name = "unit", value = "水口量(注塑)")
	@Column(length = 150)
	protected String bsWaterGap;

	/**
	 * 穴数(注塑)
	 */
	@ApiModelProperty(name = "bsCave", value = "穴数(注塑)")
	@Column(length = 150)
	protected String bsCave;

	/**
	 * 加工类型(表面处理)
	 */
	@ApiModelProperty(name = "bsMachiningType", value = "加工类型(表面处理)")
	@Column(length = 150)
	protected String bsMachiningType;

	/**
	 * 配色工艺(表面处理)
	 */
	@ApiModelProperty(name = "bsColor", value = "配色工艺(表面处理)")
	@Column(length = 150)
	protected String bsColor;

	/**
	 * 是否通用物料
	 */
	@ApiModelProperty(name = "bsGeneral", value = "是否通用物料")
	@Column(length = 10)
	protected String bsGeneral;

	/**
	 * 价格挡位
	 */
	@ApiModelProperty(name = "bsGear", value = "价格挡位")
	@Column(length = 50)
	protected String bsGear;

	/**
	 * 参考价格
	 */
	@ApiModelProperty(name = "bsGear", value = "参考价格")
	@Column(length = 50)
	protected String bsRefer;


	/**
	 * 评估价格
	 */
	@ApiModelProperty(name = "bsAssess", value = "评估价格")
	@Column(length = 50)
	protected String bsAssess;
	
	/**
	 * 总价格(未税)
	 */
	@ApiModelProperty(name = "bsFee", value = "总价格(未税)")
	@Column(length = 50)
	protected String bsFee;


	public Long getPkQuote() {
		return pkQuote;
	}

	public void setPkQuote(Long pkQuote) {
		this.pkQuote = pkQuote;
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

	public String getBsUnit() {
		return bsUnit;
	}

	public void setBsUnit(String bsUnit) {
		this.bsUnit = bsUnit;
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

	public String getBsType() {
		return bsType;
	}

	public void setBsType(String bsType) {
		this.bsType = bsType;
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

	public String getBsMachiningType() {
		return bsMachiningType;
	}

	public void setBsMachiningType(String bsMachiningType) {
		this.bsMachiningType = bsMachiningType;
	}

	public String getBsColor() {
		return bsColor;
	}

	public void setBsColor(String bsColor) {
		this.bsColor = bsColor;
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

	public String getBsGear() {
		return bsGear;
	}

	public void setBsGear(String bsGear) {
		this.bsGear = bsGear;
	}

	public String getBsQty() {
		return bsQty;
	}

	public Integer getBsPurchase() {
		return bsPurchase;
	}

	public void setBsPurchase(Integer bsPurchase) {
		this.bsPurchase = bsPurchase;
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

	public String getBsGeneral() {
		return bsGeneral;
	}

	public void setBsGeneral(String bsGeneral) {
		this.bsGeneral = bsGeneral;
	}

	public String getBsRefer() {
		return bsRefer;
	}

	public void setBsRefer(String bsRefer) {
		this.bsRefer = bsRefer;
	}

	public String getBsAssess() {
		return bsAssess;
	}

	public void setBsAssess(String bsAssess) {
		this.bsAssess = bsAssess;
	}

	public String getBsFee() {
		return bsFee;
	}

	public void setBsFee(String bsFee) {
		this.bsFee = bsFee;
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
}
