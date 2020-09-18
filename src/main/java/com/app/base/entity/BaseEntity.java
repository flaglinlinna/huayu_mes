package com.app.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity extends IdEntity {
	private static final long serialVersionUID = -5249737644031588435L;

    /**
     * 是否删除
     * 0：否 1：是
     */
	@Column
	@NotNull
	@ApiModelProperty(name="isDel",value="删除标志(0:未删除,1:已删除)")
    protected int isDel = 0;

    /**
     * 创建时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="createdTime",value="创建时间")
	protected Date createdTime;

    /**
     * 修改时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="modifiedTime",value="修改时间")
	protected Date modifiedTime;

    /**
     * 创建人
     */
    @Column
    @ApiModelProperty(name="pkCreatedBy",value="创建人Id")
    protected Long pkCreatedBy;

    /**
     * 修改人
     */
    @Column
    @ApiModelProperty(name="pkModifiedBy",value="修改人Id")
    protected Long pkModifiedBy;

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getPkCreatedBy() {
        return pkCreatedBy;
    }

    public void setPkCreatedBy(Long pkCreatedBy) {
        this.pkCreatedBy = pkCreatedBy;
    }

    public Long getPkModifiedBy() {
        return pkModifiedBy;
    }

    public void setPkModifiedBy(Long pkModifiedBy) {
        this.pkModifiedBy = pkModifiedBy;
    }
}
