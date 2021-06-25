package com.system.file.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 业务文件维护关联表(通用)
 *
 */
@Entity(name = "CommonFile")
@Table(name = CommonFile.TABLE_NAME)
@DynamicUpdate
public class CommonFile extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "common_file";

    /**
     * 业务ID
     */
    @ApiModelProperty(name = "mId", value = "业务ID")
    @Column
    protected Long mId;

    /**
     * 文件ID
     */
    @ApiModelProperty(name = "fileId", value = "文件ID")
    @Column
    protected Long fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty(name = "fileName", value = "文件名称")
    @Column
    protected String fileName;

    /**
     * 业务类别
     */
    @ApiModelProperty(name = "bsType", value = "业务类别")
    @Column
    protected String bsType;

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBsType() {
        return bsType;
    }

    public void setBsType(String bsType) {
        this.bsType = bsType;
    }
}
