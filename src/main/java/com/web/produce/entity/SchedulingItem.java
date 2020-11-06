package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.web.basic.entity.Employee;
import com.web.basic.entity.Mtrial;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
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

    @ApiModelProperty(name = "mtrial", hidden = true, value = "组件")
    @ManyToOne
    @JoinColumn(name = "itemId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Mtrial mtrial;

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

    @ApiModelProperty(name = "employee", hidden = true, value = "员工")
    @ManyToOne
    @JoinColumn(name = "empId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Employee employee;

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

    public Mtrial getMtrial() {
        return mtrial;
    }

    public void setMtrial(Mtrial mtrial) {
        this.mtrial = mtrial;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("排产父表ID:").append(this.mid);
        sb.append(",制令单号:").append(this.taskNo);
        sb.append(",组件ID:").append(this.itemId);
        sb.append(",物料编码:").append(this.itemNo);
        sb.append(",组件数量:").append(this.itemQty);
        sb.append(",组件单位:").append(this.itemUnit);
        sb.append(",单位用量:").append(this.itemQtyPr);
        sb.append(",员工id:").append(this.empId);
        sb.append(",员工编号:").append(this.empCode);
        sb.append(",良率:").append(this.fokRate);
        sb.append("\n]");
        return sb.toString();
    }
}
