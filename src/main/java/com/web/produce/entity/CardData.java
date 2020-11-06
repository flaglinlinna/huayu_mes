package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Employee;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Date;

import javax.persistence.*;

/**
 * 卡点原始数据采集
 */
@Entity(name = "CardData")
@Table(name= CardData.TABLE_NAME)
@DynamicUpdate
public class CardData extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_ATT_CARDDATA";

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
     * 卡机Id
     */
    @ApiModelProperty(name="devClockId",value="卡机Id")
    @Column
    protected Long devClockId;

    @ApiModelProperty(name="devClock",hidden=true,value="卡机Id")
    @ManyToOne
    @JoinColumn(name = "devClockId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected DevClock devClock;
    
    
    /**
     * 刷卡日期
     */
    @ApiModelProperty(name = "cardDate", value = "刷卡日期")
    @Column(length = 50)
    protected String cardDate;
   
    /**
     * 刷卡时间
     */
    @ApiModelProperty(name = "cardTime", value = "刷卡时间")
    @Column(length = 50)
    protected String cardTime;


    /**
     * 卡点状态  1(有效，默认)/0(失效)
     */  
    @ApiModelProperty(name = "fstatus", value = "卡点状态  1(有效，默认)/0(失效)")
    @Column
    protected Integer fstatus=1;
    
    /**
     * 数据类型  1(卡机数据，默认)/0(手动新增)
     */  
    @ApiModelProperty(name = "fstype", value = "数据类型  1(卡机数据，默认)/0(手动新增)")
    @Column
    protected Integer fstype=1;

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

	public Long getDevClockId() {
		return devClockId;
	}

	public void setDevClockId(Long devClockId) {
		this.devClockId = devClockId;
	}

	public DevClock getDevClock() {
		return devClock;
	}

	public void setDevClock(DevClock devClock) {
		this.devClock = devClock;
	}

	public String getCardDate() {
		return cardDate;
	}

	public void setCardDate(String cardDate) {
		this.cardDate = cardDate;
	}

	public String getCardTime() {
		return cardTime;
	}

	public void setCardTime(String cardTime) {
		this.cardTime = cardTime;
	}

	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	public Integer getFstype() {
		return fstype;
	}

	public void setFstype(Integer fstype) {
		this.fstype = fstype;
	}


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("员工id:").append(this.empId);
		sb.append(",卡机id:").append(this.devClockId);
		sb.append(",刷卡日期:").append(this.cardDate);
		sb.append(",刷卡时间:").append(this.cardTime);
		sb.append(",卡点状态:").append(this.fstatus);
		sb.append(",数据类型:").append(this.fstype);
		return sb.toString();
	}
}
