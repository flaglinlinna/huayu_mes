package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Employee;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 排产信息-生产制令单从表-工艺
 */
@Entity(name = "SchedulingProcess")
@Table(name= SchedulingProcess.TABLE_NAME)
@DynamicUpdate
public class SchedulingProcess extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_PROD_ORDER_PROC";

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
     * 工单号
     */
    @ApiModelProperty(name = "prodNo", value = "工单号")
    @Column(length = 100)
    protected String prodNo;

    /**
     * 工序编号
     */
    @ApiModelProperty(name = "procNo", value = "工序编号")
    @Column(length = 20)
    protected String procNo;

    /**
     * 工序顺序号
     */
    @ApiModelProperty(name = "procOrder", value = "工序顺序号")
    @Column(length = 20)
    protected String procOrder;

    /**
     * 抽检总量(PCS)：针对品质检验
     */
    @ApiModelProperty(name = "sampleQty", value = "抽检总量(PCS)：针对品质检验")
    @Column(precision = 22, scale = 4)
    protected BigDecimal sampleQty = BigDecimal.valueOf(0);

    /**
     * 加工数量：针对品质检验-总PCS
     */
    @ApiModelProperty(name = "qtyProc", value = "加工数量：针对品质检验-总PCS")
    @Column
    protected Integer qtyProc = 0;

    /**
     * 完工数量：针对品质检验-合格PCS
     */
    @ApiModelProperty(name = "qtyDone", value = "完工数量：针对品质检验-合格PCS")
    @Column(precision = 22, scale = 4)
    protected BigDecimal qtyDone = BigDecimal.valueOf(0);

    /**
     * 不良数量：针对品质检验-不良PCS
     */
    @ApiModelProperty(name = "defectNum", value = "不良数量：针对品质检验-不良PCS")
    @Column
    protected Integer defectNum = 0;

    /**
     * 过程属性
     */
    @ApiModelProperty(name = "jobAttr", value = "过程属性")
    @Column
    protected Integer jobAttr = 0;

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
     * 工序开始时间
     */
    @ApiModelProperty(name = "timeBegin", value = "工序开始时间")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    protected Date timeBegin;

    /**
     * 工序完成时间
     */
    @ApiModelProperty(name = "timeEnd", value = "工序完成时间")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    protected Date timeEnd;

    /**
     * 生产状态
     */
    @ApiModelProperty(name = "prodStatus", value = "生产状态")
    @Column(length = 6)
    protected String prodStatus;

    /**
     * 计划数量
     */
    @ApiModelProperty(name = "qtyPlan", value = "计划数量")
    @Column
    protected Integer qtyPlan;

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

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public String getProcNo() {
        return procNo;
    }

    public void setProcNo(String procNo) {
        this.procNo = procNo;
    }

    public String getProcOrder() {
        return procOrder;
    }

    public void setProcOrder(String procOrder) {
        this.procOrder = procOrder;
    }

    public BigDecimal getSampleQty() {
        return sampleQty;
    }

    public void setSampleQty(BigDecimal sampleQty) {
        this.sampleQty = sampleQty;
    }

    public Integer getQtyProc() {
        return qtyProc;
    }

    public void setQtyProc(Integer qtyProc) {
        this.qtyProc = qtyProc;
    }

    public BigDecimal getQtyDone() {
        return qtyDone;
    }

    public void setQtyDone(BigDecimal qtyDone) {
        this.qtyDone = qtyDone;
    }

    public Integer getDefectNum() {
        return defectNum;
    }

    public void setDefectNum(Integer defectNum) {
        this.defectNum = defectNum;
    }

    public Integer getJobAttr() {
        return jobAttr;
    }

    public void setJobAttr(Integer jobAttr) {
        this.jobAttr = jobAttr;
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

    public Date getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(Date timeBegin) {
        this.timeBegin = timeBegin;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    public Integer getQtyPlan() {
        return qtyPlan;
    }

    public void setQtyPlan(Integer qtyPlan) {
        this.qtyPlan = qtyPlan;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("排产父表ID:").append(this.mid);
        sb.append(",制令单号:").append(this.taskNo);
        sb.append(",工单号:").append(this.prodNo);
        sb.append(",工序编码:").append(this.procNo);
        sb.append(",工序顺序号:").append(this.procOrder);
        sb.append(",抽检总量:").append(this.sampleQty);
        sb.append(",加工数量:").append(this.qtyProc);
        sb.append(",完工数量:").append(this.qtyDone);
        sb.append(",不良数量:").append(this.defectNum);
        sb.append(",过程属性:").append(this.jobAttr);
        sb.append(",员工id:").append(this.empId);
        sb.append(",员工工号:").append(this.empCode);
        sb.append(",工序开始时间:").append(this.timeBegin);
        sb.append(",工序结束时间:").append(this.timeEnd);
        sb.append(",生产状态:").append(this.prodStatus);
        sb.append(",计划数量:").append(this.qtyPlan);
        return sb.toString();
    }
}
