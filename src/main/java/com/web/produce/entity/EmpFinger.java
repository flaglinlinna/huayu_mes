package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Defective;
import com.web.basic.entity.Department;
import com.web.basic.entity.Employee;
import com.web.basic.entity.Line;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.Process;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 指纹登记
 */
@Entity(name = "EmpFinger")
@Table(name= EmpFinger.TABLE_NAME)
@DynamicUpdate
public class EmpFinger extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_BASE_EMP_FINGER";
    
    
    
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
