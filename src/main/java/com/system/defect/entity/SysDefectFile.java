package com.system.defect.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 缺陷记录文件关联表
 *
 */
@Entity(name = "SysDefectFile")
@Table(name = SysDefectFile.TABLE_NAME)
@DynamicUpdate
public class SysDefectFile extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "SYS_DEFECT_FILE";

    /**
     * 缺陷记录ID
     */
    @ApiModelProperty(name = "defectId", value = "缺陷记录ID")
    @Column
    protected Long defectId;

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

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
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
