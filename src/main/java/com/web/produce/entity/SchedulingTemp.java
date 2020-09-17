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
    public static final String TABLE_NAME = "produce_scheduling_temp";

    /**
     * 部门ID
     */
    @ApiModelProperty(name = "pkDepartId", value = "部门ID")
    @Column
    protected Long pkDepartId;

    /**
     * 部门编码或名称
     */
    @ApiModelProperty(name = "bsDepartCode", value = "部门编码或名称")
    @Column(length = 100)
    protected String bsDepartCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name = "bsProduceTime", value = "生产日期")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    protected Date bsProduceTime;

    /**
     * 班次
     */
    @ApiModelProperty(name = "bsShift", value = "班次")
    @Column(length = 50)
    protected String bsShift;

    /**
     * 客户
     */
    @ApiModelProperty(name = "bsCustomer", value = "客户")
    @Column(length = 50)
    protected String bsCustomer;

    /**
     * 线别
     */
    @ApiModelProperty(name = "bsLine", value = "线别")
    @Column(length = 50)
    protected String bsLine;

    /**
     * 工单类别
     */
    @ApiModelProperty(name = "bsOrderType", value = "工单类别")
    @Column(length = 50)
    protected String bsOrderType;

    /**
     * 制令单号
     */
    @ApiModelProperty(name = "bsUniqueOrderNo", value = "制令单号")
    @Column
    protected String bsUniqueOrderNo;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "bsOrderNo", value = "工单号")
    @Column(length = 100)
    protected String bsOrderNo;

    /**
     * 生产状态
     */
    @ApiModelProperty(name = "bsStatus", value = "生产状态")
    @Column(length = 50)
    protected String bsStatus;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "pkMtrialId", value = "物料ID")
    @Column
    protected Long pkMtrialId;

    /**
     * 物料编码或名称
     */
    @ApiModelProperty(name = "bsMtrialCode", value = "物料编码或名称")
    @Column(length = 100)
    protected String bsMtrialCode;

    /**
     * 加工工艺ID（工序ID）
     */
    @ApiModelProperty(name = "pkProcId", value = "加工工艺ID（工序ID）")
    @Column
    protected Long pkProcId;

    /**
     * 加工工艺编码或名称
     */
    @ApiModelProperty(name = "bsProcName", value = "加工工艺编码或名称")
    @Column(length = 100)
    protected String bsProcName;

    /**
     * 工单残
     */
    @ApiModelProperty(name = "bsRestNum", value = "工单残")
    @Column
    protected Integer bsRestNum;

    /**
     * 计划生产数量
     */
    @ApiModelProperty(name = "bsPlanNum", value = "计划生产数量")
    @Column
    protected Integer bsPlanNum;

    /**
     * 用人量
     */
    @ApiModelProperty(name = "bsPeopleNum", value = "用人量")
    @Column
    protected Integer bsPeopleNum;

    /**
     * 产能
     */
    @ApiModelProperty(name = "bsCapacityNum", value = "产能")
    @Column
    protected Integer bsCapacityNum;

    /**
     * 预计工时(H/人)
     */
    @ApiModelProperty(name = "bsPlanHours", value = "预计工时(H/人)")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsPlanHours;

    /**
     * 实际生产数量
     */
    @ApiModelProperty(name = "bsActualNum", value = "实际生产数量")
    @Column
    protected Integer bsActualNum;

    /**
     * 实际工时(H/人)
     */
    @ApiModelProperty(name = "bsActualHours", value = "实际工时(H/人)")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsActualHours;

    /**
     * 计划金额
     */
    @ApiModelProperty(name = "bsPlanPrice", value = "计划金额")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsPlanPrice;

    /**
     * 实际金额
     */
    @ApiModelProperty(name = "bsActualPrice", value = "实际金额")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsActualPrice;

    /**
     * 备注
     */
    @ApiModelProperty(name = "bsRemark", value = "备注")
    @Column(length = 500)
    protected String bsRemark;

    public Long getPkDepartId() {
        return pkDepartId;
    }

    public void setPkDepartId(Long pkDepartId) {
        this.pkDepartId = pkDepartId;
    }

    public String getBsDepartCode() {
        return bsDepartCode;
    }

    public void setBsDepartCode(String bsDepartCode) {
        this.bsDepartCode = bsDepartCode;
    }

    public Date getBsProduceTime() {
        return bsProduceTime;
    }

    public void setBsProduceTime(Date bsProduceTime) {
        this.bsProduceTime = bsProduceTime;
    }

    public String getBsShift() {
        return bsShift;
    }

    public void setBsShift(String bsShift) {
        this.bsShift = bsShift;
    }

    public String getBsCustomer() {
        return bsCustomer;
    }

    public void setBsCustomer(String bsCustomer) {
        this.bsCustomer = bsCustomer;
    }

    public String getBsLine() {
        return bsLine;
    }

    public void setBsLine(String bsLine) {
        this.bsLine = bsLine;
    }

    public String getBsOrderType() {
        return bsOrderType;
    }

    public void setBsOrderType(String bsOrderType) {
        this.bsOrderType = bsOrderType;
    }

    public String getBsUniqueOrderNo() {
        return bsUniqueOrderNo;
    }

    public void setBsUniqueOrderNo(String bsUniqueOrderNo) {
        this.bsUniqueOrderNo = bsUniqueOrderNo;
    }

    public String getBsOrderNo() {
        return bsOrderNo;
    }

    public void setBsOrderNo(String bsOrderNo) {
        this.bsOrderNo = bsOrderNo;
    }

    public String getBsStatus() {
        return bsStatus;
    }

    public void setBsStatus(String bsStatus) {
        this.bsStatus = bsStatus;
    }

    public Long getPkMtrialId() {
        return pkMtrialId;
    }

    public void setPkMtrialId(Long pkMtrialId) {
        this.pkMtrialId = pkMtrialId;
    }

    public String getBsMtrialCode() {
        return bsMtrialCode;
    }

    public void setBsMtrialCode(String bsMtrialCode) {
        this.bsMtrialCode = bsMtrialCode;
    }

    public Long getPkProcId() {
        return pkProcId;
    }

    public void setPkProcId(Long pkProcId) {
        this.pkProcId = pkProcId;
    }

    public String getBsProcName() {
        return bsProcName;
    }

    public void setBsProcName(String bsProcName) {
        this.bsProcName = bsProcName;
    }

    public Integer getBsRestNum() {
        return bsRestNum;
    }

    public void setBsRestNum(Integer bsRestNum) {
        this.bsRestNum = bsRestNum;
    }

    public Integer getBsPlanNum() {
        return bsPlanNum;
    }

    public void setBsPlanNum(Integer bsPlanNum) {
        this.bsPlanNum = bsPlanNum;
    }

    public Integer getBsPeopleNum() {
        return bsPeopleNum;
    }

    public void setBsPeopleNum(Integer bsPeopleNum) {
        this.bsPeopleNum = bsPeopleNum;
    }

    public Integer getBsCapacityNum() {
        return bsCapacityNum;
    }

    public void setBsCapacityNum(Integer bsCapacityNum) {
        this.bsCapacityNum = bsCapacityNum;
    }

    public BigDecimal getBsPlanHours() {
        return bsPlanHours;
    }

    public void setBsPlanHours(BigDecimal bsPlanHours) {
        this.bsPlanHours = bsPlanHours;
    }

    public Integer getBsActualNum() {
        return bsActualNum;
    }

    public void setBsActualNum(Integer bsActualNum) {
        this.bsActualNum = bsActualNum;
    }

    public BigDecimal getBsActualHours() {
        return bsActualHours;
    }

    public void setBsActualHours(BigDecimal bsActualHours) {
        this.bsActualHours = bsActualHours;
    }

    public BigDecimal getBsPlanPrice() {
        return bsPlanPrice;
    }

    public void setBsPlanPrice(BigDecimal bsPlanPrice) {
        this.bsPlanPrice = bsPlanPrice;
    }

    public BigDecimal getBsActualPrice() {
        return bsActualPrice;
    }

    public void setBsActualPrice(BigDecimal bsActualPrice) {
        this.bsActualPrice = bsActualPrice;
    }

    public String getBsRemark() {
        return bsRemark;
    }

    public void setBsRemark(String bsRemark) {
        this.bsRemark = bsRemark;
    }
}
