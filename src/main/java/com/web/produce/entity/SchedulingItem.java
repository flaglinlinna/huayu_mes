package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 排产信息-生产制令单从表-组件
 */
@Entity(name = "SchedulingItem")
@Table(name= SchedulingItem.TABLE_NAME)
@DynamicUpdate
public class SchedulingItem extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_PROD_ORDER_ITEM";

    /**
     * 排产父表ID
     */
    @ApiModelProperty(name = "mid", value = "排产父表ID")
    @Column
    protected Long mid;

    /**
     * 制令单号
     */
    @ApiModelProperty(name = "taskNo", value = "制令单号")
    @Column(length = 100)
    protected String taskNo;

    /**
     * 组件ID
     */
    @ApiModelProperty(name = "itemId", value = "组件ID")
    @Column
    protected Long itemId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "itemNo", value = "物料编码")
    @Column(length = 100)
    protected String itemNo;

    /**
     * 组件数量
     */
    @ApiModelProperty(name = "itemQty", value = "组件数量")
    @Column(precision = 22, scale = 3)
    protected BigDecimal itemQty;

    /**
     * 组件单位
     */
    @ApiModelProperty(name = "itemUnit", value = "组件单位")
    @Column(length = 10)
    protected String itemUnit;

    /**
     * 单位用量
     */
    @ApiModelProperty(name = "itemQtyPr", value = "单位用量")
    @Column(precision = 22, scale = 3)
    protected BigDecimal itemQtyPr;

    /**
     * 员工ID
     */
    @ApiModelProperty(name = "empId", value = "员工ID")
    @Column
    protected Long empId;

    /**
     * 员工工号
     */
    @ApiModelProperty(name = "empCode", value = "员工工号")
    @Column(length = 50)
    protected String empCode;

    /**
     * 良率
     */
    @ApiModelProperty(name = "fokRate", value = "良率")
    @Column(precision = 22, scale = 2)
    protected BigDecimal fokRate;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public BigDecimal getItemQty() {
        return itemQty;
    }

    public void setItemQty(BigDecimal itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public BigDecimal getItemQtyPr() {
        return itemQtyPr;
    }

    public void setItemQtyPr(BigDecimal itemQtyPr) {
        this.itemQtyPr = itemQtyPr;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public BigDecimal getFokRate() {
        return fokRate;
    }

    public void setFokRate(BigDecimal fokRate) {
        this.fokRate = fokRate;
    }
}
