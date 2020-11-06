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
 * 工时异常登记处理
 */
@Entity(name = "AbnormalHours")
@Table(name= AbnormalHours.TABLE_NAME)
@DynamicUpdate
public class AbnormalHours extends BaseEntity{
	private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_ATT_ABNORMAL_HOURS";
    
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
    /*@ApiModelProperty(name="lineId",value="线体Id")
    @Column
    protected Long lineId;

    @ApiModelProperty(name="line",hidden=true,value="线体Id")
    @ManyToOne
    @JoinColumn(name = "lineId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Line line;*/
    
    /**
     * 制令单号
     */
    @ApiModelProperty(name="taskNo",value="制令单号")
    @Column(length = 50)
    protected String taskNo;
    
    /**
     * 开始时间
     */
    @ApiModelProperty(name="timeBegin",value="开始时间")
    @Column(length = 50)
    protected String timeBegin;
    
    /**
     * 结束时间
     */
    @ApiModelProperty(name="timeEnd",value="结束时间")
    @Column(length = 50)
    protected String timeEnd;  
    
    /**
     * 时长
     */
    @ApiModelProperty(name="duration",value="时长")
    protected Float duration;  
    
    /**
     * 异常描述
     */
    @ApiModelProperty(name="description",value="异常描述")
    @Column(length = 500)
    protected String description;
    
    /**
     * 异常原因
     */
    @ApiModelProperty(name="forReason",value="异常原因")
    @Column(length = 50)
    protected String forReason;
    
    
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

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getForReason() {
		return forReason;
	}

	public void setForReason(String forReason) {
		this.forReason = forReason;
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

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}
	

	/*public Long getLineId() {
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
	}*/

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("员工Id:").append(this.empId);
		sb.append(",制令单号:").append(this.taskNo);
		sb.append(",开始时间:").append(this.timeBegin);
		sb.append(",结束时间:").append(this.timeEnd);
		sb.append(",时长:").append(this.duration);
		sb.append(",描述:").append(this.description);
		sb.append(",异常原因:").append(this.forReason);
		sb.append(",审核标识:").append(this.checkStatus);
		sb.append(",审核人:").append(this.checkBy);
		sb.append(",审核时间:").append(this.checkDate);
		return sb.toString();
	}
}
