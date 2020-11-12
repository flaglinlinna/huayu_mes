package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 排产信息(导入制令单) 新 从表 2020/11/12
 */
@Entity(name = "SchedulingDet")
@Table(name= SchedulingDet.TABLE_NAME)
@DynamicUpdate
public class SchedulingDet extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901263L;
    public static final String TABLE_NAME = "MES_PROD_ORDER_IMP_DET";

    /**
     * 主表ID(SchedulingMain)
     */
    @ApiModelProperty(name = "mid", value = "主表ID(SchedulingMain)")
    @Column
    protected Long mid;

    /**
     * 组合
     */
    @ApiModelProperty(name = "groupNo", value = "组合")
    @Column
    protected Integer groupNo;

    /**
     * 客户名称
     */
    @ApiModelProperty(name = "custName", value = "客户名称")
    @Column(length = 50)
    protected String custName;

    /**
     * 客户ID
     */
    @ApiModelProperty(name = "custId", value = "客户ID")
    @Column(length = 50)
    protected String custId;

    /**
     * 客户编码
     */
    @ApiModelProperty(name = "custNo", value = "客户编码")
    @Column(length = 50)
    protected String custNo;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "prodNo", value = "工单号")
    @Column(length = 100)
    protected String prodNo;

    /**
     * 计划数量
     */
    @ApiModelProperty(name = "qtyPlan", value = "计划数量")
    @Column
    protected Integer qtyPlan;

    /**
     * 线长名称
     */
    @ApiModelProperty(name = "linerName", value = "线长名称")
    @Column(length = 50)
    protected String linerName;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "itemName", value = "物料名称")
    @Column(length = 500)
    protected String itemName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "itemNo", value = "物料编码")
    @Column(length = 100)
    protected String itemNo;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "itemId", value = "物料ID")
    @Column(length = 50)
    protected String itemId;

    /**
     * 校验状态（0：校验通过 / 1：校验不通过 / 2：警告）
     */
    @ApiModelProperty(name = "checkStatus", value = "校验状态（0：校验通过 / 1：校验不通过 / 2：警告）")
    @Column(length = 1)
    protected Integer checkStatus = 0;

    /**
     * 错误信息
     */
    @ApiModelProperty(name = "errorInfo", value = "错误信息")
    @Column(length = 1000)
    protected String errorInfo;

    /**
     * 是否已生效(0:未生效 / 1:已生效)
     */
    @ApiModelProperty(name = "enabled", value = "是否已生效(0:未生效 / 1:已生效)")
    @Column(length = 1)
    protected Integer enabled = 0;

    /**
     * 生产制令单
     */
    @ApiModelProperty(name = "taskNo", value = "生产制令单")
    @Column(length = 200)
    protected String taskNo;

    /**
     * 生产制令单ID
     */
    @ApiModelProperty(name = "taskId", value = "生产制令单ID")
    @Column
    protected Long taskId;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public Integer getQtyPlan() {
        return qtyPlan;
    }

    public void setQtyPlan(Integer qtyPlan) {
        this.qtyPlan = qtyPlan;
    }

    public String getLinerName() {
        return linerName;
    }

    public void setLinerName(String linerName) {
        this.linerName = linerName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
