package com.web.po.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 接口信息配置表
 *
 */
@Entity(name = "Interfaces")
@Table(name= Interfaces.TABLE_NAME)
@DynamicUpdate
public class Interfaces extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "api_interfaces";

    /**
     * 接口编号
     */
    @ApiModelProperty(name = "bsCode", value = "接口编号")
    @Column
    protected String bsCode;

    /**
     * 接口名称
     */
    @ApiModelProperty(name = "bsName", value = "接口名称")
    @Column
    protected String bsName;

    /**
     * 状态（0:禁用 / 1:启用）
     */
    @ApiModelProperty(name = "bsStatus", value = "状态（0:禁用 / 1:启用）")
    @Column
    protected Integer bsStatus = 1;

    /**
     * 请求方式（POST,GET,PUT,DELETE）
     */
    @ApiModelProperty(name = "bsMethod", value = "请求方式（POST,GET,PUT,DELETE）")
    @Column(length = 100)
    protected String bsMethod;

    /**
     * 请求地址
     */
    @ApiModelProperty(name = "bsUrl", value = "请求地址")
    @Column
    protected String bsUrl;

    /**
     * 请求参数（以{}封装）
     */
    @ApiModelProperty(name = "bsParam", value = "请求参数（以{}封装）")
    @Column
    protected String bsParam;

    /**
     * 执行方式（0:手动执行 / 1:定时执行）
     */
    @ApiModelProperty(name = "bsType", value = "执行方式（0:手动执行 / 1:定时执行）")
    @Column
    protected Integer bsType = 0;

    /**
     * 执行开始日期
     */
    @ApiModelProperty(name = "startDate", value = "执行开始日期")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column
    protected Date startDate;

    /**
     * 执行截止日期
     */
    @ApiModelProperty(name = "endDate", value = "执行截止日期")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column
    protected Date endDate;

    /**
     * 执行计划的定时方式
     */
    @ApiModelProperty(name = "timingCode", value = "执行计划的定时方式")
    @Column(length = 100)
    protected String timingCode;

    /**
     * 备注
     */
    @ApiModelProperty(name = "bsRemark", value = "备注")
    @Column(length = 500)
    protected String bsRemark;

    public String getBsCode() {
        return bsCode;
    }

    public void setBsCode(String bsCode) {
        this.bsCode = bsCode;
    }

    public String getBsName() {
        return bsName;
    }

    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public Integer getBsStatus() {
        return bsStatus;
    }

    public void setBsStatus(Integer bsStatus) {
        this.bsStatus = bsStatus;
    }

    public String getBsMethod() {
        return bsMethod;
    }

    public void setBsMethod(String bsMethod) {
        this.bsMethod = bsMethod;
    }

    public String getBsUrl() {
        return bsUrl;
    }

    public void setBsUrl(String bsUrl) {
        this.bsUrl = bsUrl;
    }

    public String getBsParam() {
        return bsParam;
    }

    public void setBsParam(String bsParam) {
        this.bsParam = bsParam;
    }

    public Integer getBsType() {
        return bsType;
    }

    public void setBsType(Integer bsType) {
        this.bsType = bsType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTimingCode() {
        return timingCode;
    }

    public void setTimingCode(String timingCode) {
        this.timingCode = timingCode;
    }

    public String getBsRemark() {
        return bsRemark;
    }

    public void setBsRemark(String bsRemark) {
        this.bsRemark = bsRemark;
    }
}
