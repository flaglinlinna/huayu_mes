package com.web.produce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Employee;
import com.web.basic.entity.Line;

import io.swagger.annotations.ApiModelProperty;

/**
 * 补卡处理
 */
@Entity(name = "PatchCard")
@Table(name= PatchCard.TABLE_NAME)
@DynamicUpdate
public class PatchCard extends BaseEntity{
	private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_ATT_SIGN_CARD";
    
    /**
     * 员工Id
     */
    @ApiModelProperty(name="empId",value="员工Id")
    @Column
    protected Long empId;

    @ApiModelProperty(name="emp",hidden=true,value="员工Id")
    @ManyToOne
    @JoinColumn(name = "empId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Employee employee;
    
    /**
     * 线体Id
     */
    @ApiModelProperty(name="lineId",value="线体Id")
    @Column
    protected Long lineId;

    @ApiModelProperty(name="line",hidden=true,value="线体Id")
    @ManyToOne
    @JoinColumn(name = "lineId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Line line;
    
    /**
     * 班次Id
     */
    @ApiModelProperty(name="classId",value="班次Id")
    @Column
    protected Long classId;

    @ApiModelProperty(name="classType",hidden=true,value="班次Id")
    @ManyToOne
    @JoinColumn(name = "classId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected ClassType classType;   
    
    
    /**
     * 工时类型
     */
    @ApiModelProperty(name="hourType",value="工时类型")
    @Column(length = 100)
    protected String hourType;
    
    /**
     * 制令单号
     */
    @ApiModelProperty(name="taskNo",value="制令单号")
    @Column(length = 50)
    protected String taskNo;
    
    /**
     * 卡点类型
     */
    @ApiModelProperty(name="cardType",value="卡点类型")
    @Column(length = 10)
    protected String cardType;
    
    /**
     * 生产日期
     */
    @ApiModelProperty(name="workDate",value="生产日期")
    @Column(length = 20)
    protected String workDate;
    
    /**
     * 签卡时间
     */
    @ApiModelProperty(name="signTime",value="签卡时间")
    @Column(length = 20)
    protected String signTime;
    
    /**
     * 签卡日期
     */
    @ApiModelProperty(name="signDate",value="签卡日期")
    @Column(length = 20)
    protected String signDate;
    
    /**
     * 审核标识  1(有效)/0(无效，默认)
     */  
    @ApiModelProperty(name = "checkStatus", value = "卡点状态  1(有效)/0(无效，默认)")
    @Column
    protected Integer checkStatus=0;
    
    /**
     * 审核人
     */
    @ApiModelProperty(name="checkBy",value="审核人")
    @Column(length = 50)
    protected String checkBy;
    
    /**
     * 审核时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="checkDate",value="审核时间")
	protected Date checkDate;

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

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getHourType() {
		return hourType;
	}

	public void setHourType(String hourType) {
		this.hourType = hourType;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public ClassType getClassType() {
		return classType;
	}

	public void setClassType(ClassType classType) {
		this.classType = classType;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("员工Id:").append(this.empId);
		sb.append(",线体id:").append(this.lineId);
		sb.append(",班次id:").append(this.classId);
		sb.append(",时间类型:").append(this.hourType);
		sb.append(",指令单号:").append(this.taskNo);
		sb.append(",卡点类型:").append(this.cardType);
		sb.append(",生产日期:").append(this.workDate);
		sb.append(",签卡时间:").append(this.signTime);
		sb.append(",签卡日期:").append(this.signDate);
		sb.append(",审核标识:").append(this.checkStatus);
		sb.append(",审核人:").append(this.checkBy);
		sb.append(",审核时间:").append(this.checkDate);
		return sb.toString();
	}
}
