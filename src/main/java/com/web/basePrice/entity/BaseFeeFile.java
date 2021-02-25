package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 人工制费文件维护关联表
 *
 */
@Entity(name = "BaseFeeFile")
@Table(name = BaseFeeFile.TABLE_NAME)
@DynamicUpdate
public class BaseFeeFile extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "BJ_BASE_BaseFee_FILE";

    /**
     * 人工制费文件ID
     */
    @ApiModelProperty(name = "mId", value = "人工制费文件ID")
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
}
