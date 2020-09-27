package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 工序基础信息表
 *
 */
@Entity(name = "Process")
@Table(name = Process.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Process extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "MES_base_PROC";

    /**
     * 工序编码
     */
    @ApiModelProperty(name = "procNo", value = "工序编码")
    @Column(length = 50)
    protected String procNo;

    /**
     * 工序名称
     */
    @ApiModelProperty(name = "procName", value = "工序名称")
    @Column(length = 50)
    protected String procName;

    /**
     * 工序顺序号
     */
    @ApiModelProperty(name = "procOrder", value = "工序顺序号")
    @Column(length = 5)
    protected Integer procOrder;

    /**
     * 状态
     */
    @ApiModelProperty(name = "checkStatus", value = "状态")
    @Column(length = 1)
    protected Integer checkStatus = 0;

    public String getProcNo() {
        return procNo;
    }

    public void setProcNo(String procNo) {
        this.procNo = procNo;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public Integer getProcOrder() {
        return procOrder;
    }

    public void setProcOrder(Integer procOrder) {
        this.procOrder = procOrder;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }
}
