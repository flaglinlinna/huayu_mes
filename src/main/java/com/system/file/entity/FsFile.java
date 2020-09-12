package com.system.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;

@Table(
        name = "fs_file"
)
@Entity
@DynamicUpdate
public class FsFile extends BaseEntity {
    private static final long serialVersionUID = -5554295976916560513L;
    public static final String TABLE_NAME = "fs_file";
    protected Long pkFsCatalog;
    @Column(
            length = 150
    )
    protected String bsContentType;
    @Column(
            length = 25
    )
    protected String bsFileType;
    @Column(
            length = 220
    )
    protected String bsName;
    @Column(
            length = 220
    )
    protected String bsFileName;
    protected Long bsFileSize = 0L;
    @Column(
            length = 255
    )
    protected String bsFilePath;
    protected Integer bsIsValid;
    @ManyToOne
    @JoinColumn(
            name = "pkFsCatalog",
            insertable = false,
            updatable = false
    )
    @NotFound(
            action = NotFoundAction.IGNORE
    )
    protected FsCatalog catalog;

    public FsFile() {
        this.bsIsValid = 0;
    }

    public Long getPkFsCatalog() {
        return this.pkFsCatalog;
    }

    public void setPkFsCatalog(Long pkFsCatalog) {
        this.pkFsCatalog = pkFsCatalog;
    }

    public String getBsContentType() {
        return this.bsContentType;
    }

    public void setBsContentType(String bsContentType) {
        this.bsContentType = bsContentType;
    }

    public String getBsFileType() {
        return this.bsFileType;
    }

    public void setBsFileType(String bsFileType) {
        this.bsFileType = bsFileType;
    }

    public String getBsName() {
        return this.bsName;
    }

    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public String getBsFileName() {
        return this.bsFileName;
    }

    public void setBsFileName(String bsFileName) {
        this.bsFileName = bsFileName;
    }

    public Long getBsFileSize() {
        return this.bsFileSize;
    }

    public void setBsFileSize(Long bsFileSize) {
        this.bsFileSize = bsFileSize;
    }

    public String getBsFilePath() {
        return this.bsFilePath;
    }

    public void setBsFilePath(String bsFilePath) {
        this.bsFilePath = bsFilePath;
    }

    public Integer getBsIsValid() {
        return this.bsIsValid;
    }

    public void setBsIsValid(Integer bsIsValid) {
        this.bsIsValid = bsIsValid;
    }

    public FsCatalog getCatalog() {
        return this.catalog;
    }

    public void setCatalog(FsCatalog catalog) {
        this.catalog = catalog;
    }
}
