package com.web.quote.entity;

import java.math.BigDecimal;
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
import com.system.user.entity.SysUser;

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
     * 1-业务部状态
     * 0：草稿，
     * 1:进行中
     * 2:已完成
     * 99:关闭
     */
	@Column
	@NotNull
	@ApiModelProperty(name="bsStatus",value="状态")
    protected int bsStatus = 0;
	
	/**
     * 2-制造部+采购部+外协部的状态
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus2",value="状态")
    protected int bsStatus2 = 0;
	
	/**
     * 2-制造部状态-五金
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus2Hardware",value="状态")
    protected int bsStatus2Hardware = 0;
	/**
     * 2-制造部状态-注塑
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus2Molding",value="状态")
    protected int bsStatus2Molding = 0;
	/**
     * 2-制造部状态-表面处理
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus2Surface",value="状态")
    protected int bsStatus2Surface = 0;
	/**
     * 2-制造部状态-组装
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus2Packag",value="状态")
    protected int bsStatus2Packag = 0;
	
	/**
     * 2-采购部状态
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus2Surface",value="状态")
    protected int bsStatus2Purchase = 0;
	/**
     * 2-外协部状态
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus2Out",value="状态")
    protected int bsStatus2Out = 0;
	
	/**
     * 3-业务部状态
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus3",value="状态")
    protected int bsStatus3 = 0;
	
	/**
     * 4-总经理审批状态
     * 0:草稿
     * 1:进行中
     * 2:已完成
     */
	@Column
	@ApiModelProperty(name="bsStatus5",value="状态")
    protected int bsStatus4 = 0;


	/**
	 * 虚拟料号对应状态
	 * 0:进行中
	 * 1:已完成
	 */
	@Column
	@ApiModelProperty(name="bsStatusItem",value="虚拟料号对应状态")
	protected Integer bsStatusItem = 0;
	
	/**
     * 流程步骤
     * 1:业务部
     * 2:制造部+采购部+外协部
     * 3:业务部
     * 4:总经理
     */
	@Column
	@ApiModelProperty(name="bsStep",value="流程步骤")
    protected int bsStep = 1;

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
	 * 报价阶段
	 * (初始报价 、量产报价)
	 */
	@ApiModelProperty(name = "bsStage", value = "报价阶段")
	@Column(length = 100)
	protected String bsStage;

	/**
	 * 是否最新
	 * 0否 1是
	 */
	@ApiModelProperty(name = "bsLatest", value = "是否最新")
	@Column(length = 100)
	protected Integer bsLatest = 0;

	/**
	 * 是否中标
	 * 0否 1是
	 */
	@ApiModelProperty(name = "bsBade", value = "是否中标")
	@Column(length = 100)
	protected Integer bsBade = 0;


	
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
	 * 产品类型id
	 * **/	
	@ApiModelProperty(name="bsProdTypeId",value="产品类型id")
    @Column
    protected Long bsProdTypeId;
	
	/**
	 * 关联产品利润率维护表
	 * **/	
	/*@ApiModelProperty(name="pkProfitProd",value="产品利润率表ID")
    @Column
    protected Long pkProfitProd;

    @ApiModelProperty(name="profitProd",hidden=true,value="产品利润率表")
    @ManyToOne
    @JoinColumn(name = "pkProfitProd", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected ProfitProd profitProd;*/
	
	/**
	 * 产品利润率
	 */
	@ApiModelProperty(name = "bsProfitProd", value = "产品利润率")
	@Column(length = 20)
	protected BigDecimal bsProfitProd;

	/**
	 * 净利润(手工录入)
	 */
	@ApiModelProperty(name = "bsProfitNet", value = "净利润")
	@Column(length = 20)
	protected BigDecimal bsProfitNet;
	
	
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
	 * 管理费用
	 */
	@ApiModelProperty(name = "bsManageFee", value = "管理费用")
	@Column(length = 20)
	protected BigDecimal bsManageFee;
	
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
	
	/**
	 * 结束时间
	 */	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsEndTime1",value="结束时间")
	protected Date bsEndTime1;
	
	/**
	 * 结束时间
	 */	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsEndTime2",value="结束时间")
	protected Date bsEndTime2;
	
	/**
	 * 结束时间
	 */	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsEndTime3",value="结束时间")
	protected Date bsEndTime3;
	
	/**
	 * 结束时间
	 */	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsEndTime4",value="结束时间")
	protected Date bsEndTime4;
	
	/**
	 * 结束时间
	 */	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsEndTime5",value="结束时间")
	protected Date bsEndTime5;
	
	/**
	 * 结束时间
	 */	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="bsEndTime6",value="结束时间")
	protected Date bsEndTime6;
	
	
	
	@ApiModelProperty(name="user",hidden=true,value="用户表")
    @ManyToOne
    @JoinColumn(name = "createBy", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected SysUser user;

	/**
	 * 复制报价单的Id
	 * **/
	@ApiModelProperty(name="bsCopyId",value="复制报价单的Id")
	@Column
	protected Long bsCopyId;


	public BigDecimal getBsProfitNet() {
		return bsProfitNet;
	}

	public void setBsProfitNet(BigDecimal bsProfitNet) {
		this.bsProfitNet = bsProfitNet;
	}

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
	

	public BigDecimal getBsProfitProd() {
		return bsProfitProd;
	}

	public void setBsProfitProd(BigDecimal bsProfitProd) {
		this.bsProfitProd = bsProfitProd;
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

	public int getBsStep() {
		return bsStep;
	}

	public void setBsStep(int bsStep) {
		this.bsStep = bsStep;
	}

	public Date getBsEndTime1() {
		return bsEndTime1;
	}

	public void setBsEndTime1(Date bsEndTime1) {
		this.bsEndTime1 = bsEndTime1;
	}

	public Date getBsEndTime2() {
		return bsEndTime2;
	}

	public void setBsEndTime2(Date bsEndTime2) {
		this.bsEndTime2 = bsEndTime2;
	}

	public Date getBsEndTime3() {
		return bsEndTime3;
	}

	public void setBsEndTime3(Date bsEndTime3) {
		this.bsEndTime3 = bsEndTime3;
	}

	public Date getBsEndTime4() {
		return bsEndTime4;
	}

	public void setBsEndTime4(Date bsEndTime4) {
		this.bsEndTime4 = bsEndTime4;
	}

	public Date getBsEndTime5() {
		return bsEndTime5;
	}

	public void setBsEndTime5(Date bsEndTime5) {
		this.bsEndTime5 = bsEndTime5;
	}

	public Date getBsEndTime6() {
		return bsEndTime6;
	}

	public void setBsEndTime6(Date bsEndTime6) {
		this.bsEndTime6 = bsEndTime6;
	}

	public int getBsStatus2() {
		return bsStatus2;
	}

	public void setBsStatus2(int bsStatus2) {
		this.bsStatus2 = bsStatus2;
	}

	public int getBsStatus3() {
		return bsStatus3;
	}

	public void setBsStatus3(int bsStatus3) {
		this.bsStatus3 = bsStatus3;
	}

	public int getBsStatus4() {
		return bsStatus4;
	}

	public void setBsStatus4(int bsStatus4) {
		this.bsStatus4 = bsStatus4;
	}

	public int getBsStatus2Hardware() {
		return bsStatus2Hardware;
	}

	public void setBsStatus2Hardware(int bsStatus2Hardware) {
		this.bsStatus2Hardware = bsStatus2Hardware;
	}

	public int getBsStatus2Molding() {
		return bsStatus2Molding;
	}

	public void setBsStatus2Molding(int bsStatus2Molding) {
		this.bsStatus2Molding = bsStatus2Molding;
	}

	public int getBsStatus2Surface() {
		return bsStatus2Surface;
	}

	public void setBsStatus2Surface(int bsStatus2Surface) {
		this.bsStatus2Surface = bsStatus2Surface;
	}

	public int getBsStatus2Packag() {
		return bsStatus2Packag;
	}

	public Integer getBsStatusItem() {
		return bsStatusItem;
	}

	public void setBsStatusItem(Integer bsStatusItem) {
		this.bsStatusItem = bsStatusItem;
	}

	public void setBsStatus2Packag(int bsStatus2Packag) {
		this.bsStatus2Packag = bsStatus2Packag;
	}

	public BigDecimal getBsManageFee() {
		return bsManageFee;
	}

	public void setBsManageFee(BigDecimal bsManageFee) {
		this.bsManageFee = bsManageFee;
	}

	public int getBsStatus2Purchase() {
		return bsStatus2Purchase;
	}

	public void setBsStatus2Purchase(int bsStatus2Purchase) {
		this.bsStatus2Purchase = bsStatus2Purchase;
	}

	public int getBsStatus2Out() {
		return bsStatus2Out;
	}

	public void setBsStatus2Out(int bsStatus2Out) {
		this.bsStatus2Out = bsStatus2Out;
	}

	public Long getBsProdTypeId() {
		return bsProdTypeId;
	}

	public void setBsProdTypeId(Long bsProdTypeId) {
		this.bsProdTypeId = bsProdTypeId;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public String getBsStage() {
		return bsStage;
	}

	public void setBsStage(String bsStage) {
		this.bsStage = bsStage;
	}

	public Integer getBsLatest() {
		return bsLatest;
	}

	public void setBsLatest(Integer bsLatest) {
		this.bsLatest = bsLatest;
	}

	public Integer getBsBade() {
		return bsBade;
	}

	public void setBsBade(Integer bsBade) {
		this.bsBade = bsBade;
	}

	public Long getBsCopyId() {
		return bsCopyId;
	}

	public void setBsCopyId(Long bsCopyId) {
		this.bsCopyId = bsCopyId;
	}
}
