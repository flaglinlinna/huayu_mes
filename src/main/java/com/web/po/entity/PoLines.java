package com.web.po.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;

/**
 * PO信息表
 *
 */
@Entity(name = "PoLines")
@Table(name = PoLines.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class PoLines extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "api_po_lines";

    /**
     * PO发运行ID
     */
    @ApiModelProperty(name = "lineLocationId", value = "PO发运行ID")
    @NotNull
    @Column(columnDefinition = "bigint(20) comment 'PO发运行ID'")
    protected Long lineLocationId;

    /**
     * ERP服务器ID
     */
    @ApiModelProperty(name = "instanceId", value = "ERP服务器ID")
    @NotNull
    @Column(columnDefinition = "bigint(20) comment 'ERP服务器ID'")
    protected Long instanceId;

    /**
     * 采购员
     */
    @ApiModelProperty(name = "agentName", value = "采购员")
    @Column(columnDefinition = "varchar(255) comment '采购员'")
    protected String agentName;

    /**
     * 附件
     */
    @ApiModelProperty(name = "attachmentQty", value = "附件")
    @Column(columnDefinition = "int(11) comment '附件'")
    protected Integer attachmentQty;

    /**
     * 招标区域
     */
    @ApiModelProperty(name = "biddingArea", value = "招标区域")
    @Column(columnDefinition = "varchar(255) comment '招标区域'")
    protected String biddingArea;

    /**
     * 开票地址
     */
    @ApiModelProperty(name = "billToLocation", value = "开票地址")
    @Column(columnDefinition = "varchar(255) comment '开票地址'")
    protected String billToLocation;

    /**
     * 采购模式
     */
    @ApiModelProperty(name = "businessMode", value = "采购模式")
    @Column(columnDefinition = "varchar(255) comment '采购模式'")
    protected String businessMode;

    /**
     * 发运方式
     */
    @ApiModelProperty(name = "carrierName", value = "发运方式")
    @Column(columnDefinition = "varchar(255) comment '发运方式'")
    protected String carrierName;

    /**
     * 品类
     */
    @ApiModelProperty(name = "category", value = "品类")
    @Column(columnDefinition = "varchar(255) comment '品类'")
    protected String category;

    /**
     * 币种
     */
    @ApiModelProperty(name = "currencyCode", value = "币种")
    @Column(columnDefinition = "varchar(255) comment '币种'")
    protected String currencyCode;

    /**
     * 未交付数量
     */
    @ApiModelProperty(name = "dueQty", value = "未交付数量")
    @Column(columnDefinition = "int(11) comment '未交付数量'")
    protected Integer dueQty;

    /**
     * 工程号
     */
    @ApiModelProperty(name = "engineeringNo", value = "工程号")
    @Column(columnDefinition = "varchar(255) comment '工程号'")
    protected String engineeringNo;

    /**
     * 产品大类
     */
    @ApiModelProperty(name = "engInfo", value = "产品大类")
    @Column(columnDefinition = "varchar(255) comment '产品大类'")
    protected String engInfo;

    /**
     * 母局
     */
    @ApiModelProperty(name = "engInfoCentralZone", value = "母局")
    @Column(columnDefinition = "varchar(255) comment '母局'")
    protected String engInfoCentralZone;

    /**
     * 项目名称/工程名称
     */
    @ApiModelProperty(name = "engInfoEngineeringName", value = "项目名称/工程名称")
    @Column(columnDefinition = "varchar(255) comment '项目名称/工程名称'")
    protected String engInfoEngineeringName;

    /**
     * 销售合同号
     */
    @ApiModelProperty(name = "engInfoSalesContractNo", value = "销售合同号")
    @Column(columnDefinition = "varchar(255) comment '销售合同号'")
    protected String engInfoSalesContractNo;

    /**
     * 例外信息释放时间
     */
    @ApiModelProperty(name = "exceptionUpdatedDate", value = "例外信息释放时间")
    @Column(columnDefinition = "varchar(255) comment '例外信息释放时间'")
    protected String exceptionUpdatedDate;

    /**
     * 订单有效日期
     */
    @ApiModelProperty(name = "expireDate", value = "订单有效日期")
    @Column(columnDefinition = "varchar(255) comment '订单有效日期'")
    protected String expireDate;

    /**
     * 首次承诺日期
     */
    @ApiModelProperty(name = "firstPromiseDate", value = "首次承诺日期")
    @Column(columnDefinition = "varchar(255) comment '首次承诺日期'")
    protected String firstPromiseDate;

    /**
     * 开票单位
     */
    @ApiModelProperty(name = "issuOffice", value = "开票单位")
    @Column(columnDefinition = "varchar(255) comment '开票单位'")
    protected String issuOffice;

    /**
     * Item编码
     */
    @ApiModelProperty(name = "itemCode", value = "Item编码")
    @Column(columnDefinition = "varchar(255) comment 'Item编码'")
    protected String itemCode;

    /**
     * Item描述
     */
    @ApiModelProperty(name = "itemDescription", value = "Item描述")
    @Column(columnDefinition = "varchar(255) comment 'Item描述'")
    protected String itemDescription;

    /**
     * 物料/服务编码版本
     */
    @ApiModelProperty(name = "itemRevision", value = "物料/服务编码版本")
    @Column(columnDefinition = "varchar(255) comment '物料/服务编码版本'")
    protected String itemRevision;

    /**
     * 生产：生产厂家/工程：站点信息
     */
    @ApiModelProperty(name = "manufactureSiteInfo", value = "生产：生产厂家/工程：站点信息")
    @Column(columnDefinition = "varchar(255) comment '生产：生产厂家/工程：站点信息'")
    protected String manufactureSiteInfo;

    /**
     * 服务结束日期/完工日期
     */
    @ApiModelProperty(name = "needByDate", value = "服务结束日期/完工日期")
    @Column(columnDefinition = "varchar(255) comment '服务结束日期/完工日期'")
    protected String needByDate;

    /**
     * 接受日期
     */
    @ApiModelProperty(name = "openDate", value = "接受日期")
    @Column(columnDefinition = "varchar(255) comment '接受日期'")
    protected String openDate;

    /**
     * 待签返任务单
     */
    @ApiModelProperty(name = "openTaskQuantity", value = "待签返任务单")
    @Column(columnDefinition = "int(11) comment '待签返任务单'")
    protected Integer openTaskQuantity;

    /**
     * 总任务单
     */
    @ApiModelProperty(name = "taskQuantity", value = "总任务单")
    @Column(columnDefinition = "int(255) comment '总任务单'")
    protected Integer taskQuantity;

    /**
     * 华为子公司
     */
    @ApiModelProperty(name = "orgName", value = "华为子公司")
    @Column(columnDefinition = "varchar(255) comment '华为子公司'")
    protected String orgName;

    /**
     * 厂家型号
     */
    @ApiModelProperty(name = "partNumber", value = "厂家型号")
    @Column(columnDefinition = "varchar(255) comment '厂家型号'")
    protected String partNumber;

    /**
     * 支付条款
     */
    @ApiModelProperty(name = "paymentTerms", value = "支付条款")
    @Column(columnDefinition = "varchar(255) comment '支付条款'")
    protected String paymentTerms;

    /**
     * 给验收人备注
     */
    @ApiModelProperty(name = "pllaNoteToReceiver", value = "给验收人备注")
    @Column(columnDefinition = "varchar(255) comment '给验收人备注'")
    protected String pllaNoteToReceiver;

    /**
     * 订单行号
     */
    @ApiModelProperty(name = "poLineNum", value = "订单行号")
    @Column(columnDefinition = "varchar(255) comment '订单行号'")
    protected String poLineNum;

    /**
     * PO号
     */
    @ApiModelProperty(name = "poNumber", value = "PO号")
    @Column(columnDefinition = "varchar(255) comment 'PO号'")
    protected String poNumber;

    /**
     * 单价
     */
    @ApiModelProperty(name = "priceOverride", value = "单价")
    @Column(columnDefinition = "decimal(15,3) comment '单价'")
    protected BigDecimal priceOverride;

    /**
     * PR号
     */
    @ApiModelProperty(name = "prNumber", value = "PR号")
    @Column(columnDefinition = "varchar(255) comment 'PR号'")
    protected String prNumber;

    /**
     * 项目编码
     */
    @ApiModelProperty(name = "projectNo", value = "项目编码")
    @Column(columnDefinition = "varchar(255) comment '项目编码'")
    protected String projectNo;

    /**
     * 承诺日期(页面显示为需求日期)
     */
    @ApiModelProperty(name = "promiseDate", value = "承诺日期(页面显示为需求日期)")
    @Column(columnDefinition = "varchar(255) comment '承诺日期(页面显示为需求日期)'")
    protected String promiseDate;

    /**
     * 订单下发日期
     */
    @ApiModelProperty(name = "publishDate", value = "订单下发日期")
    @Column(columnDefinition = "varchar(255) comment '订单下发日期'")
    protected String publishDate;

    /**
     * 接收人
     */
    @ApiModelProperty(name = "receiver", value = "接收人")
    @Column(columnDefinition = "varchar(255) comment '接收人'")
    protected String receiver;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @Column(columnDefinition = "varchar(255) comment '备注'")
    protected String remark;

    /**
     * 代表处
     */
    @ApiModelProperty(name = "repOfficeName", value = "代表处")
    @Column(columnDefinition = "varchar(255) comment '代表处'")
    protected String repOfficeName;

    /**
     * 版本号
     */
    @ApiModelProperty(name = "revisionNum", value = "版本号")
    @Column(columnDefinition = "varchar(255) comment '版本号'")
    protected String revisionNum;

    /**
     * 发运行号
     */
    @ApiModelProperty(name = "shipmentNum", value = "发运行号")
    @Column(columnDefinition = "varchar(255) comment '发运行号'")
    protected String shipmentNum;

    /**
     * 订单状态
     */
    @ApiModelProperty(name = "shipmentStatus", value = "订单状态")
    @Column(columnDefinition = "varchar(255) comment '订单状态'")
    protected String shipmentStatus;

    /**
     * 收货地点
     */
    @ApiModelProperty(name = "shipToLocation", value = "收货地点")
    @Column(columnDefinition = "varchar(255) comment '收货地点'")
    protected String shipToLocation;

    /**
     * 收货地点code
     */
    @ApiModelProperty(name = "shipToLocationCode", value = "收货地点code")
    @Column(columnDefinition = "varchar(255) comment '收货地点code'")
    protected String shipToLocationCode;

    /**
     * 开工日期
     */
    @ApiModelProperty(name = "startDate", value = "开工日期")
    @Column(columnDefinition = "varchar(255) comment '开工日期'")
    protected String startDate;

    /**
     * 采购协议号
     */
    @ApiModelProperty(name = "subcontractNo", value = "采购协议号")
    @Column(columnDefinition = "varchar(255) comment '采购协议号'")
    protected String subcontractNo;

    /**
     * 子项目编码
     */
    @ApiModelProperty(name = "subProjectCode", value = "子项目编码")
    @Column(columnDefinition = "varchar(255) comment '子项目编码'")
    protected String subProjectCode;

    /**
     * 任务令
     */
    @ApiModelProperty(name = "taskNum", value = "任务令")
    @Column(columnDefinition = "varchar(255) comment '任务令'")
    protected String taskNum;

    /**
     * 税率
     */
    @ApiModelProperty(name = "taxRate", value = "税率")
    @Column(columnDefinition = "varchar(255) comment '税率'")
    protected String taxRate;

    /**
     * 付款方式
     */
    @ApiModelProperty(name = "termsMode", value = "付款方式")
    @Column(columnDefinition = "varchar(255) comment '付款方式'")
    protected String termsMode;

    /**
     * 单位
     */
    @ApiModelProperty(name = "unitOfMeasure", value = "单位")
    @Column(columnDefinition = "varchar(255) comment '单位'")
    protected String unitOfMeasure;

    /**
     * 供应商编码
     */
    @ApiModelProperty(name = "vendorCode", value = "供应商编码")
    @Column(columnDefinition = "varchar(255) comment '供应商编码'")
    protected String vendorCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name = "vendorName", value = "供应商名称")
    @Column(columnDefinition = "varchar(255) comment '供应商名称'")
    protected String vendorName;

    /**
     * 给供应商备注（特殊备注）
     */
    @ApiModelProperty(name = "vendorShortText", value = "给供应商备注（特殊备注）")
    @Column(columnDefinition = "varchar(255) comment '给供应商备注（特殊备注）'")
    protected String vendorShortText;

    /**
     * 华为型号
     */
    @ApiModelProperty(name = "hwm", value = "华为型号")
    @Column(columnDefinition = "varchar(255) comment '华为型号'")
    protected String hwm;

    /**
     * 订单数量
     */
    @ApiModelProperty(name = "quantity", value = "订单数量")
    @Column(columnDefinition = "int(11) comment '订单数量'")
    protected Integer quantity;

    /**
     * 取消数量
     */
    @ApiModelProperty(name = "quantityCancelled", value = "取消数量")
    @Column(columnDefinition = "int(11) comment '取消数量'")
    protected Integer quantityCancelled;

    /**
     * 接收数量
     */
    @ApiModelProperty(name = "quantityReceived", value = "接收数量")
    @Column(columnDefinition = "int(11) comment '接收数量'")
    protected Integer quantityReceived;

    /**
     * 变更LIST
     */
    @ApiModelProperty(name = "objectChangeContext", value = "变更LIST")
    @Column(columnDefinition = "varchar(255) comment '变更LIST'")
    protected String objectChangeContext;

    /**
     * 到货计划列表
     */
    @ApiModelProperty(name = "arrivalPlanList", value = "到货计划列表")
    @Column(columnDefinition = "varchar(255) comment '到货计划列表'")
    protected String arrivalPlanList;

    public Long getLineLocationId() {
        return lineLocationId;
    }

    public void setLineLocationId(Long lineLocationId) {
        this.lineLocationId = lineLocationId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getAttachmentQty() {
        return attachmentQty;
    }

    public void setAttachmentQty(Integer attachmentQty) {
        this.attachmentQty = attachmentQty;
    }

    public String getBiddingArea() {
        return biddingArea;
    }

    public void setBiddingArea(String biddingArea) {
        this.biddingArea = biddingArea;
    }

    public String getBillToLocation() {
        return billToLocation;
    }

    public void setBillToLocation(String billToLocation) {
        this.billToLocation = billToLocation;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getDueQty() {
        return dueQty;
    }

    public void setDueQty(Integer dueQty) {
        this.dueQty = dueQty;
    }

    public String getEngineeringNo() {
        return engineeringNo;
    }

    public void setEngineeringNo(String engineeringNo) {
        this.engineeringNo = engineeringNo;
    }

    public String getEngInfo() {
        return engInfo;
    }

    public void setEngInfo(String engInfo) {
        this.engInfo = engInfo;
    }

    public String getEngInfoCentralZone() {
        return engInfoCentralZone;
    }

    public void setEngInfoCentralZone(String engInfoCentralZone) {
        this.engInfoCentralZone = engInfoCentralZone;
    }

    public String getEngInfoEngineeringName() {
        return engInfoEngineeringName;
    }

    public void setEngInfoEngineeringName(String engInfoEngineeringName) {
        this.engInfoEngineeringName = engInfoEngineeringName;
    }

    public String getEngInfoSalesContractNo() {
        return engInfoSalesContractNo;
    }

    public void setEngInfoSalesContractNo(String engInfoSalesContractNo) {
        this.engInfoSalesContractNo = engInfoSalesContractNo;
    }

    public String getExceptionUpdatedDate() {
        return exceptionUpdatedDate;
    }

    public void setExceptionUpdatedDate(String exceptionUpdatedDate) {
        this.exceptionUpdatedDate = exceptionUpdatedDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getFirstPromiseDate() {
        return firstPromiseDate;
    }

    public void setFirstPromiseDate(String firstPromiseDate) {
        this.firstPromiseDate = firstPromiseDate;
    }

    public String getIssuOffice() {
        return issuOffice;
    }

    public void setIssuOffice(String issuOffice) {
        this.issuOffice = issuOffice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemRevision() {
        return itemRevision;
    }

    public void setItemRevision(String itemRevision) {
        this.itemRevision = itemRevision;
    }

    public String getManufactureSiteInfo() {
        return manufactureSiteInfo;
    }

    public void setManufactureSiteInfo(String manufactureSiteInfo) {
        this.manufactureSiteInfo = manufactureSiteInfo;
    }

    public String getNeedByDate() {
        return needByDate;
    }

    public void setNeedByDate(String needByDate) {
        this.needByDate = needByDate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public Integer getOpenTaskQuantity() {
        return openTaskQuantity;
    }

    public void setOpenTaskQuantity(Integer openTaskQuantity) {
        this.openTaskQuantity = openTaskQuantity;
    }

    public Integer getTaskQuantity() {
        return taskQuantity;
    }

    public void setTaskQuantity(Integer taskQuantity) {
        this.taskQuantity = taskQuantity;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getPllaNoteToReceiver() {
        return pllaNoteToReceiver;
    }

    public void setPllaNoteToReceiver(String pllaNoteToReceiver) {
        this.pllaNoteToReceiver = pllaNoteToReceiver;
    }

    public String getPoLineNum() {
        return poLineNum;
    }

    public void setPoLineNum(String poLineNum) {
        this.poLineNum = poLineNum;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public BigDecimal getPriceOverride() {
        return priceOverride;
    }

    public void setPriceOverride(BigDecimal priceOverride) {
        this.priceOverride = priceOverride;
    }

    public String getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(String prNumber) {
        this.prNumber = prNumber;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getPromiseDate() {
        return promiseDate;
    }

    public void setPromiseDate(String promiseDate) {
        this.promiseDate = promiseDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepOfficeName() {
        return repOfficeName;
    }

    public void setRepOfficeName(String repOfficeName) {
        this.repOfficeName = repOfficeName;
    }

    public String getRevisionNum() {
        return revisionNum;
    }

    public void setRevisionNum(String revisionNum) {
        this.revisionNum = revisionNum;
    }

    public String getShipmentNum() {
        return shipmentNum;
    }

    public void setShipmentNum(String shipmentNum) {
        this.shipmentNum = shipmentNum;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getShipToLocation() {
        return shipToLocation;
    }

    public void setShipToLocation(String shipToLocation) {
        this.shipToLocation = shipToLocation;
    }

    public String getShipToLocationCode() {
        return shipToLocationCode;
    }

    public void setShipToLocationCode(String shipToLocationCode) {
        this.shipToLocationCode = shipToLocationCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSubcontractNo() {
        return subcontractNo;
    }

    public void setSubcontractNo(String subcontractNo) {
        this.subcontractNo = subcontractNo;
    }

    public String getSubProjectCode() {
        return subProjectCode;
    }

    public void setSubProjectCode(String subProjectCode) {
        this.subProjectCode = subProjectCode;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTermsMode() {
        return termsMode;
    }

    public void setTermsMode(String termsMode) {
        this.termsMode = termsMode;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorShortText() {
        return vendorShortText;
    }

    public void setVendorShortText(String vendorShortText) {
        this.vendorShortText = vendorShortText;
    }

    public String getHwm() {
        return hwm;
    }

    public void setHwm(String hwm) {
        this.hwm = hwm;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityCancelled() {
        return quantityCancelled;
    }

    public void setQuantityCancelled(Integer quantityCancelled) {
        this.quantityCancelled = quantityCancelled;
    }

    public Integer getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public String getObjectChangeContext() {
        return objectChangeContext;
    }

    public void setObjectChangeContext(String objectChangeContext) {
        this.objectChangeContext = objectChangeContext;
    }

    public String getArrivalPlanList() {
        return arrivalPlanList;
    }

    public void setArrivalPlanList(String arrivalPlanList) {
        this.arrivalPlanList = arrivalPlanList;
    }
}
