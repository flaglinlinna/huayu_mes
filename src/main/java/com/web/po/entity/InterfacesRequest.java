package com.web.po.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 接口信息配置-请求参数表
 *
 */
@Entity(name = "InterfacesRequest")
@Table(name= InterfacesRequest.TABLE_NAME)
@DynamicUpdate
public class InterfacesRequest extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "api_interfaces_request";

    /**
     * 接口信息主表ID
     */
    @ApiModelProperty(name = "interId", value = "接口信息主表ID")
    @Column
    protected Long interId;

    /**
     * 参数名称
     */
    @ApiModelProperty(name = "bsName", value = "参数名称")
    @Column(length = 100)
    protected String bsName;

    /**
     * 参数类型
     */
    @ApiModelProperty(name = "bsType", value = "参数类型")
    @Column(length = 100)
    protected String bsType;

    /**
     * 描述
     */
    @ApiModelProperty(name = "bsDescpt", value = "描述")
    @Column
    protected String bsDescpt;

    /**
     * 参数值
     */
    @ApiModelProperty(name = "bsValue", value = "参数值")
    @Column
    protected String bsValue;

    /**
     * 是否必填（0:否 / 1:是）
     */
    @ApiModelProperty(name = "isRequired", value = "是否必填（0:否 / 1:是）")
    @Column
    protected Integer isRequired = 0;

    public Long getInterId() {
        return interId;
    }

    public void setInterId(Long interId) {
        this.interId = interId;
    }

    public String getBsName() {
        return bsName;
    }

    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public String getBsType() {
        return bsType;
    }

    public void setBsType(String bsType) {
        this.bsType = bsType;
    }

    public String getBsDescpt() {
        return bsDescpt;
    }

    public void setBsDescpt(String bsDescpt) {
        this.bsDescpt = bsDescpt;
    }

    public String getBsValue() {
        return bsValue;
    }

    public void setBsValue(String bsValue) {
        this.bsValue = bsValue;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }
}
