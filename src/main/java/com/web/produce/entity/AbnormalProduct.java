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
 * 生产异常
 */
@Entity(name = "AbnormalProduct")
@Table(name= AbnormalProduct.TABLE_NAME)
@DynamicUpdate
public class AbnormalProduct extends BaseEntity{
	private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_PROD_ORDER_ERR";

    
    /**
     * 制令单号
     */
    @ApiModelProperty(name="taskNo",value="制令单号")
    @Column(length = 50)
    protected String taskNo;
    
    /**
     * 登记时间
     */
    @ApiModelProperty(name="ftime",value="登记时间")
    @Column(length = 50)
    protected String ftime;

	/**
	 * 解除时间
	 */
	@ApiModelProperty(name="releaseTime",value="解除时间")
	@Column(length = 50)
	protected String releaseTime;
    
    
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
	
	
	/**
     * 产线id
     */  
    @ApiModelProperty(name = "LineId", value = "产线id")
    @Column
    protected Long LineId;
    
    /**
     * 工时时长
     */  
    @ApiModelProperty(name = "ftimeLong", value = "工时时长")
    @Column
    protected Integer ftimeLong=0;
    
    /**
     * 异常类型
     */
    @ApiModelProperty(name="ftype",value="异常类型")
    @Column(length = 100)
    protected String ftype;

	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public String getFtime() {
		return ftime;
	}
	public void setFtime(String ftime) {
		this.ftime = ftime;
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
	public Long getLineId() {
		return LineId;
	}
	public void setLineId(Long lineId) {
		LineId = lineId;
	}




	public Integer getFtimeLong() {
		return ftimeLong;
	}




	public void setFtimeLong(Integer ftimeLong) {
		this.ftimeLong = ftimeLong;
	}




	public String getFtype() {
		return ftype;
	}




	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append(",制令单号:").append(this.taskNo);
		sb.append(",登记时间:").append(this.ftime);
		sb.append(",时长:").append(this.ftimeLong);
		sb.append(",描述:").append(this.description);
		sb.append(",异常原因:").append(this.forReason);
		sb.append(",审核标识:").append(this.checkStatus);
		sb.append(",审核人:").append(this.checkBy);
		sb.append(",审核时间:").append(this.checkDate);
		return sb.toString();
	}
}
