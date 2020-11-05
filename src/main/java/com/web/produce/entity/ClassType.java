package com.web.produce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 补卡处理
 */
@Entity(name = "ClassType")
@Table(name= ClassType.TABLE_NAME)
@DynamicUpdate
public class ClassType extends BaseEntity{
	private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_BASE_CLASS_TYPE";
    
    /**
     * 班次编码
     */
    @ApiModelProperty(name="classNo",value="班次编码")
    @Column(length = 20)
    protected String classNo;
    
    /**
     * 班次名称
     */
    @ApiModelProperty(name="className",value="班次名称")
    @Column(length = 20)
    protected String className;
    
    /**
     * 上班时间
     */
    @ApiModelProperty(name="fcard1",value="上班时间")
    @Column(length = 10)
    protected String fcard1;
    
    /**
     * 下班时间
     */
    @ApiModelProperty(name="fcard2",value="下班时间")
    @Column(length = 10)
    protected String fcard2;

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFcard1() {
		return fcard1;
	}

	public void setFcard1(String fcard1) {
		this.fcard1 = fcard1;
	}

	public String getFcard2() {
		return fcard2;
	}

	public void setFcard2(String fcard2) {
		this.fcard2 = fcard2;
	}


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append(",classNo:").append(this.classNo);
		sb.append(",className:").append(this.className);
		sb.append(",fcard1:").append(this.fcard1);
		sb.append(",fcard2:").append(this.fcard2);
		sb.append("\n]");
		return sb.toString();
	}
}
