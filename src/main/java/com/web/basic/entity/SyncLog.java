package com.web.basic.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据同步日志表
 * <p>用于记录同步时间做增量更新</p>
 *
 */
@Entity(name = "SyncLog")
@Table(name = SyncLog.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SyncLog extends BaseEntity {
    private static final long serialVersionUID = 6944849575214769761L;
    public static final String TABLE_NAME = "t_sync_log";

    /**
     * 编码
     * @see com.utils.enumeration.SyncModuleEnum
     */
    @ApiModelProperty(name = "bsCode", value = "编码")
    @NotNull
    @Column(length = 50)
    protected String bsCode;

    /**
     * 名称
     */
    @ApiModelProperty(name = "bsName", value = "名称")
    @NotNull
    @Column(length = 100)
    protected String bsName;

    /**
     * 上次同步时间
     */
    @ApiModelProperty(name = "bsLastSyncTime", value = "上次同步时间")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column
    protected Date bsLastSyncTime;

    /**
     * 同步状态（0：同步失败 / 1：同步成功）
     */
    @ApiModelProperty(name = "bsStatus", value = "同步状态（0：同步成功 / 1：同步失败）")
    @Column
    protected Integer bsStatus;

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

    public Date getBsLastSyncTime() {
        return bsLastSyncTime;
    }

    public void setBsLastSyncTime(Date bsLastSyncTime) {
        this.bsLastSyncTime = bsLastSyncTime;
    }

    public Integer getBsStatus() {
        return bsStatus;
    }

    public void setBsStatus(Integer bsStatus) {
        this.bsStatus = bsStatus;
    }

    public String getBsRemark() {
        return bsRemark;
    }

    public void setBsRemark(String bsRemark) {
        this.bsRemark = bsRemark;
    }
}
