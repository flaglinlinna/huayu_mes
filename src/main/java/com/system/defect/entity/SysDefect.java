package com.system.defect.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 缺陷记录
 */
@Entity(name = "Defect")
@Table(name= SysDefect.TABLE_NAME)
@DynamicUpdate
public class SysDefect extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "SYS_DEFECT";

    /**
     * 模块名称
     */
    @ApiModelProperty(name = "moduleName", value = "模块名称")
    @Column
    protected String moduleName;

    /**
     * 优先级
     */
    @ApiModelProperty(name = "priority", value = "优先级")
    @Column
    protected Integer priority = 0;

    /**
     * 状态（未处理，进行中，已完成）
     */
    @ApiModelProperty(name = "status", value = "状态")
    @Column
    protected String status;

    /**
     * 问题描述
     */
    @ApiModelProperty(name = "descript", value = "问题描述")
    @Column(length = 1000)
    protected String descript;

    /**
     * 提出人
     */
    @ApiModelProperty(name = "offerName", value = "提出人")
    @Column
    protected String offerName;

    /**
     * 提出日期
     */
    @ApiModelProperty(name = "offerDate", value = "提出日期")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @Column
    protected Date offerDate;

    /**
     * 处理人
     */
    @ApiModelProperty(name = "handlerName", value = "处理人")
    @Column
    protected String handlerName;

    /**
     * 解决日期
     */
    @ApiModelProperty(name = "handlerDate", value = "解决日期")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @Column
    protected Date handlerDate;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @Column(length = 1000)
    protected String remark;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public Date getHandlerDate() {
        return handlerDate;
    }

    public void setHandlerDate(Date handlerDate) {
        this.handlerDate = handlerDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
