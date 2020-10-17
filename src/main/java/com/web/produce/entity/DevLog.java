package com.web.produce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;
import com.system.user.entity.SysUser;
import com.web.basic.entity.Employee;

import io.swagger.annotations.ApiModelProperty;

/**
 * 卡机操作日志表
 */
@Entity(name = "DevLog")
@Table(name= DevLog.TABLE_NAME)
@DynamicUpdate
public class DevLog extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_DEV_OPER_LOG";

    /**
     * 操作描述
     */
    @ApiModelProperty(name = "description", value = "操作描述")
    @Column(length = 500)
    protected String description;
    
    /**
     * 员工ID
     */
    @ApiModelProperty(name="empId",value="员工ID")
    @Column
    protected Long empId;

    @ApiModelProperty(name="employee",hidden=true,value="员工信息")
    @ManyToOne
    @JoinColumn(name = "empId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Employee emp;
    
    /**
     * 卡机IP
     */
    @ApiModelProperty(name = "devIp", value = "卡机IP")
    @Column(length = 100)
    protected String devIp;
    
    /**
     * 卡机Id
     */
    @ApiModelProperty(name="devId",value="卡机Id")
    @Column
    protected Long devId;

    @ApiModelProperty(name="devClock",hidden=true,value="卡机Id")
    @ManyToOne
    @JoinColumn(name = "devId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected DevClock devClock;
    
    @ApiModelProperty(name="sysUser",hidden=true,value="操作人")
    @ManyToOne
    @JoinColumn(name = "createBy", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected SysUser createUser;
    
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public DevClock getDevClock() {
		return devClock;
	}

	public void setDevClock(DevClock devClock) {
		this.devClock = devClock;
	}

	public String getDevIp() {
		return devIp;
	}

	public void setDevIp(String devIp) {
		this.devIp = devIp;
	}

	public SysUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(SysUser createUser) {
		this.createUser = createUser;
	}
	
	

}
