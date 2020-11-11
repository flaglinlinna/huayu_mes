package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Employee;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指纹登记
 */
@Entity(name = "EmpFinger")
@Table(name= EmpFinger.TABLE_NAME)
@DynamicUpdate
public class EmpFinger extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "V_BASE_EMP_FINGER";
    
    

	/**
	 * 最后下发时间
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="lastOffDate",value="最后下发时间")
	protected Date lastOffDate;
    
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
     * 指纹模板
     */
    @ApiModelProperty(name = "templateStr", value = "指纹模板")
    @Column(length = 3000)
    protected String templateStr;

    /**
     * 手指序号
     */
    @ApiModelProperty(name = "fingerIdx", value = "手指序号")
    @Column(length = 50)
    protected String fingerIdx;

    /**
     * 指纹权限(备用)
     */
    
    @ApiModelProperty(name = "指纹权限(备用)", value = "指纹权限(备用)")
    @Column
    protected Integer fprivilege=0;

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

	public String getTemplateStr() {
		return templateStr;
	}

	public void setTemplateStr(String templateStr) {
		this.templateStr = templateStr;
	}

	public String getFingerIdx() {
		return fingerIdx;
	}

	public void setFingerIdx(String fingerIdx) {
		this.fingerIdx = fingerIdx;
	}

	public Integer getFprivilege() {
		return fprivilege;
	}

	public void setFprivilege(Integer fprivilege) {
		this.fprivilege = fprivilege;
	}

	public Date getLastOffDate() {
		return lastOffDate;
	}

	public void setLastOffDate(Date lastOffDate) {
		this.lastOffDate = lastOffDate;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("员工id:").append(this.empId);
		sb.append(",指纹模板:").append(this.templateStr);
		sb.append(",手指序号:").append(this.fingerIdx);
		sb.append(",指纹权限:").append(this.fprivilege);
		return sb.toString();
	}
}
