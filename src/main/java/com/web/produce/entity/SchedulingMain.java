package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 排产信息 新 主表 2020/11/11
 */
@Entity(name = "SchedulingMain")
@Table(name= SchedulingMain.TABLE_NAME)
@DynamicUpdate
public class SchedulingMain extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901263L;
    public static final String TABLE_NAME = "MES_PROD_ORDER_IMP_MAIN";

    /**
     * 生产日期
     */
    @ApiModelProperty(name = "prodDate", value = "生产日期")
    @Column(length = 100)
    protected String prodDate;

    /**
     * 部门名称
     */
    @ApiModelProperty(name = "deptName", value = "部门名称")
    @Column
    protected String deptName;

    /**
     * 部门ID
     */
    @ApiModelProperty(name = "deptId", value = "部门ID")
    @Column(length = 50)
    protected String deptId;

    /**
     * 班次名称
     */
    @ApiModelProperty(name = "className", value = "班次名称")
    @Column(length = 50)
    protected String className;

    /**
     * 是否生效 0否 1是
     */
    @ApiModelProperty(name = "fenable", value = "是否生效 0否 1是")
    @Column(name = "fenable", length = 50)
    protected Integer fenable = 0;

    /**
     * 生效人
     */
    @ApiModelProperty(name = "fenableBy", value = "生效人")
    @Column(length = 50)
    protected String fenableBy;

    /**
     * 生效时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @ApiModelProperty(name="fenableDate",value="生效时间")
    protected Date fenableDate;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "idNo", value = "工单号")
    @Column(length = 50)
    protected String idNo;

    public String getProdDate() {
        return prodDate;
    }

    public void setProdDate(String prodDate) {
        this.prodDate = prodDate;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getFenable() {
        return fenable;
    }

    public void setFenable(Integer fenable) {
        this.fenable = fenable;
    }

    public String getFenableBy() {
        return fenableBy;
    }

    public void setFenableBy(String fenableBy) {
        this.fenableBy = fenableBy;
    }

    public Date getFenableDate() {
        return fenableDate;
    }

    public void setFenableDate(Date fenableDate) {
        this.fenableDate = fenableDate;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
