package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 排产信息导入临时表
 */
@Entity(name = "SchedulingTemp")
@Table(name= SchedulingTemp.TABLE_NAME)
@DynamicUpdate
public class SchedulingTemp extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_PROD_ORDER_IMP";

    /**
     * 组合
     */
    @ApiModelProperty(name = "groupNo", value = "组合")
    @Column
    protected Integer groupNo;

    /**
     * 客户ID
     */
    @ApiModelProperty(name = "custId", value = "客户ID")
    @Column(length = 50)
    protected String custId;

    /**
     * 客户名称
     */
    @ApiModelProperty(name = "custName", value = "客户名称")
    @Column(length = 50)
    protected String custName;

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
     * 物料ID
     */
    @ApiModelProperty(name = "itemId", value = "物料ID")
    @Column(length = 50)
    protected String itemId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "itemNo", value = "物料编码")
    @Column(length = 100)
    protected String itemNo;

    /**
     * 物料全称
     */
    @ApiModelProperty(name = "itemName", value = "物料全称")
    @Column(length = 500)
    protected String itemName;

    /**
     * 计划数量
     */
    @ApiModelProperty(name = "qtyPlan", value = "计划数量")
    @Column
    protected Integer qtyPlan;

    /**
     * 生产日期
     */
    @ApiModelProperty(name = "prodDate", value = "生产日期")
    @Column(length = 100)
    protected String prodDate;

    /**
     * 部门ID
     */
    @ApiModelProperty(name = "deptId", value = "部门ID")
    @Column(length = 100)
    protected String deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(name = "deptName", value = "部门名称")
    @Column(length = 100)
    protected String deptName;

    /**
     * 线长名称
     */
    @ApiModelProperty(name = "linerName", value = "线长名称")
    @Column(length = 50)
    protected String linerName;

    /**
     * 班次
     */
    @ApiModelProperty(name = "classNo", value = "班次")
    @Column(length = 50)
    protected String classNo;

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

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQtyPlan() {
        return qtyPlan;
    }

    public void setQtyPlan(Integer qtyPlan) {
        this.qtyPlan = qtyPlan;
    }

    public String getProdDate() {
        return prodDate;
    }

    public void setProdDate(String prodDate) {
        this.prodDate = prodDate;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLinerName() {
        return linerName;
    }

    public void setLinerName(String linerName) {
        this.linerName = linerName;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("组合:").append(this.groupNo);
        sb.append(",客户id:").append(this.custId);
        sb.append(",客户名称:").append(this.custName);
        sb.append(",客户编号:").append(this.custNo);
        sb.append(",工单号:").append(this.prodNo);
        sb.append(",物料id:").append(this.itemId);
        sb.append(",物料编号:").append(this.itemNo);
        sb.append(",物料名称:").append(this.itemName);
        sb.append(",计划数量:").append(this.qtyPlan);
        sb.append(",生产日期:").append(this.prodDate);
        sb.append(",部门id:").append(this.deptId);
        sb.append(",部门名称:").append(this.deptName);
        sb.append(",线长名称:").append(this.linerName);
        sb.append(",班次:").append(this.classNo);
        sb.append(",错误信息:").append(this.errorInfo);
        sb.append(",校验状态:").append(this.checkStatus);
        return sb.toString();
    }
}
