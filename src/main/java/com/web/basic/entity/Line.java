package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 线体基础信息表
 *
 */
@Entity(name = "Line")
@Table(name = Line.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Line extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "basic_line";

    /**
     * 线体编码
     */
    @ApiModelProperty(name = "lineNo", value = "线体编码")
    @Column(length = 50)
    protected String lineNo;

    /**
     * 线体名称
     */
    @ApiModelProperty(name = "lineName", value = "线体名称")
    @Column(length = 50)
    protected String lineName;

    /**
     * 状态
     */
    @ApiModelProperty(name = "checkStatus", value = "状态")
    @Column(length = 1)
    protected Integer checkStatus = 0;

    /**
     * 线长工号
     */
    @ApiModelProperty(name = "linerCode", value = "线长工号")
    @Column(length = 50)
    protected String linerCode;

    /**
     * 线长姓名
     */
    @ApiModelProperty(name = "linerName", value = "线长姓名")
    @Column(length = 50)
    protected String linerName;

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getLinerCode() {
        return linerCode;
    }

    public void setLinerCode(String linerCode) {
        this.linerCode = linerCode;
    }

    public String getLinerName() {
        return linerName;
    }

    public void setLinerName(String linerName) {
        this.linerName = linerName;
    }
}
