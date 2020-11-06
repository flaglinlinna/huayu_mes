package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Employee;
import com.web.basic.entity.Line;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Date;

import javax.persistence.*;

/**
 * 上线人员
 */
@Entity(name = "OnlineStaff")
@Table(name= OnlineStaff.TABLE_NAME)
@DynamicUpdate
public class OnlineStaff extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_LINE_AFFIRM";

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
     * 制令单号
     */
    @ApiModelProperty(name = "taskNo", value = "制令单号")
    @Column(length = 100)
    protected String taskNo;
   
    /**
     * 时间类型
     */
    @ApiModelProperty(name = "hourType", value = "时间类型")
    @Column(length = 100)
    protected String hourType;
    
    /**
     * 班次Id
     */
    @ApiModelProperty(name="classId",value="班次Id")
    @Column
    protected Long classId;
    
    /**
     * 生产日期
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	@ApiModelProperty(name="workDate",value="生产日期")
	protected Date workDate;

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

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getHourType() {
		return hourType;
	}

	public void setHourType(String hourType) {
		this.hourType = hourType;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("线体id:").append(this.lineId);
		sb.append(",制令单号:").append(this.taskNo);
		sb.append(",时间类型:").append(this.hourType);
		sb.append(",班次Id:").append(this.classId);
		sb.append(",生产日期:").append(this.workDate);
		return sb.toString();
	}
}
