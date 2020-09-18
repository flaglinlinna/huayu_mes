package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Department;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.WoProc;
import com.web.po.entity.Interfaces;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 排产信息
 */
@Entity(name = "Scheduling")
@Table(name= Scheduling.TABLE_NAME)
@DynamicUpdate
public class Scheduling extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "produce_scheduling";

    /**
     * 部门ID
     */
    @ApiModelProperty(name = "pkDepartment", value = "部门ID")
    @Column
    protected Long pkDepartment;

    @ApiModelProperty(name = "department", hidden = true, value = "部门")
    @ManyToOne
    @JoinColumn(name = "pkDepartment", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Department department;

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
    @ApiModelProperty(name = "pkMtrial", value = "物料ID")
    @Column
    protected Long pkMtrial;

    @ApiModelProperty(name = "mtrial", hidden = true, value = "物料")
    @ManyToOne
    @JoinColumn(name = "pkMtrial", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Mtrial mtrial;

    /**
     * 加工工艺ID（工序ID）
     */
    @ApiModelProperty(name = "pkWoProc", value = "加工工艺ID（工序ID）")
    @Column
    protected Long pkWoProc;

    @ApiModelProperty(name = "woProc", hidden = true, value = "加工工艺")
    @ManyToOne
    @JoinColumn(name = "pkWoProc", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected WoProc woProc;

    /**
     * 工单残
     */
    @ApiModelProperty(name = "bsRestNum", value = "工单残")
    @Column
    protected String bsRestNum;

    /**
     * 计划生产数量
     */
    @ApiModelProperty(name = "bsPlanNum", value = "计划生产数量")
    @Column
    protected String bsPlanNum;

    /**
     * 用人量
     */
    @ApiModelProperty(name = "bsPeopleNum", value = "用人量")
    @Column
    protected String bsPeopleNum;

    /**
     * 产能
     */
    @ApiModelProperty(name = "bsCapacityNum", value = "产能")
    @Column
    protected String bsCapacityNum;

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
    protected String bsActualNum;

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

    public Long getPkDepartment() {
        return pkDepartment;
    }

    public void setPkDepartment(Long pkDepartment) {
        this.pkDepartment = pkDepartment;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public Long getPkMtrial() {
        return pkMtrial;
    }

    public void setPkMtrial(Long pkMtrial) {
        this.pkMtrial = pkMtrial;
    }

    public Mtrial getMtrial() {
        return mtrial;
    }

    public void setMtrial(Mtrial mtrial) {
        this.mtrial = mtrial;
    }

    public Long getPkWoProc() {
        return pkWoProc;
    }

    public void setPkWoProc(Long pkWoProc) {
        this.pkWoProc = pkWoProc;
    }

    public WoProc getWoProc() {
        return woProc;
    }

    public void setWoProc(WoProc woProc) {
        this.woProc = woProc;
    }

    public String getBsRestNum() {
        return bsRestNum;
    }

    public void setBsRestNum(String bsRestNum) {
        this.bsRestNum = bsRestNum;
    }

    public String getBsPlanNum() {
        return bsPlanNum;
    }

    public void setBsPlanNum(String bsPlanNum) {
        this.bsPlanNum = bsPlanNum;
    }

    public String getBsPeopleNum() {
        return bsPeopleNum;
    }

    public void setBsPeopleNum(String bsPeopleNum) {
        this.bsPeopleNum = bsPeopleNum;
    }

    public String getBsCapacityNum() {
        return bsCapacityNum;
    }

    public void setBsCapacityNum(String bsCapacityNum) {
        this.bsCapacityNum = bsCapacityNum;
    }

    public BigDecimal getBsPlanHours() {
        return bsPlanHours;
    }

    public void setBsPlanHours(BigDecimal bsPlanHours) {
        this.bsPlanHours = bsPlanHours;
    }

    public String getBsActualNum() {
        return bsActualNum;
    }

    public void setBsActualNum(String bsActualNum) {
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
