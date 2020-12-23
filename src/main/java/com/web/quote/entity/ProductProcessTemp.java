package com.web.quote.entity;

import com.app.base.entity.BaseEntity;
import com.web.basePrice.entity.Proc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 报价工艺流程表 (导入临时表)
 *
 */
@Entity(name = "ProductProcessTemp")
@Table(name = ProductProcessTemp.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProductProcessTemp extends BaseEntity {
	
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_product_process_temp";
	
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
     * 关联主表
     */
    @ApiModelProperty(name="pkQuote",value="报价主表")
    @Column
    protected Long pkQuote;

	/**
	 * 校验状态（0：校验通过 / 1：校验不通过 / 2：警告）
	 */
	@ApiModelProperty(name = "checkStatus", value = "校验状态（0：校验通过 / 1：校验不通过 / 2：警告）")
	@Column(length = 1)
	protected Integer checkStatus = 0;

	/**
	 * 是否导入(0:未导入 / 1:已导入)
	 */
	@ApiModelProperty(name = "enabled", value = "是否已生效(0:未生效 / 1:已生效)")
	@Column(length = 1)
	protected Integer enabled = 0;

	/**
	 * 错误信息
	 */
	@ApiModelProperty(name = "errorInfo", value = "错误信息")
	@Column(length = 1000)
	protected String errorInfo;

	/**
	 * 主表id
	 */
	@ApiModelProperty(name = "mid", value = "主表id")
	@Column(length = 20)
	protected Long mid;

    @ApiModelProperty(name="quote",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkQuote", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Quote quote;
    
    /**
	 * Bom零件名称
	 */
	@ApiModelProperty(name = "bsName", value = "Bom零件名称")
	@Column(length = 100)
	protected String bsName;
    
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
	@Column(length = 50)
	protected String bsOrder;
	
	/**
	 * 机台类型
	 */
	@ApiModelProperty(name = "bsModelType", value = "机台类型")
	@Column(length = 100)
	protected String bsModelType;
	
	/**
	 * 基数
	 */
	@ApiModelProperty(name = "bsRadix", value = "基数")
	@Column(length = 50)
	protected String bsRadix;
	
	/**
	 * 人数
	 */
	@ApiModelProperty(name = "bsUserNum", value = "人数")
	@Column(length = 50)
	protected String bsUserNum;
	
	/**
	 * 成型周期
	 */
	@ApiModelProperty(name = "bsCycle", value = "成型周期")
	@Column(length = 50)
	protected String bsCycle;
	
	/**
	 * 工序良率(%)
	 */
	@ApiModelProperty(name = "bsYield", value = "工序良率(%)")
	@Column(length = 50)
	protected String bsYield;


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


	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public String getBsType() {
		return bsType;
	}

	public void setBsType(String bsType) {
		this.bsType = bsType;
	}

	public Long getPkQuote() {
		return pkQuote;
	}

	public void setPkQuote(Long pkQuote) {
		this.pkQuote = pkQuote;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
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

	public String getBsOrder() {
		return bsOrder;
	}

	public void setBsOrder(String bsOrder) {
		this.bsOrder = bsOrder;
	}

	public String getBsModelType() {
		return bsModelType;
	}

	public void setBsModelType(String bsModelType) {
		this.bsModelType = bsModelType;
	}

	public String getBsRadix() {
		return bsRadix;
	}

	public void setBsRadix(String bsRadix) {
		this.bsRadix = bsRadix;
	}

	public String getBsUserNum() {
		return bsUserNum;
	}

	public void setBsUserNum(String bsUserNum) {
		this.bsUserNum = bsUserNum;
	}

	public String getBsCycle() {
		return bsCycle;
	}

	public void setBsCycle(String bsCycle) {
		this.bsCycle = bsCycle;
	}

	public String getBsYield() {
		return bsYield;
	}

	public void setBsYield(String bsYield) {
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
}
