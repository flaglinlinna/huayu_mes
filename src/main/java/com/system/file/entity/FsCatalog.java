package com.system.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.app.base.entity.TreeEntity;

@Table
@Entity
public class FsCatalog extends TreeEntity<Long> {
    private static final long serialVersionUID = -6602711639299311787L;
    protected String bsUrl;
    protected String bsComment;
    @Column
    protected boolean bsSys;
    @Transient
    protected FsCatalog bsParent;

    public FsCatalog() {
    }

    public String getBsUrl() {
        return this.bsUrl;
    }

    public void setBsUrl(String bsUrl) {
        this.bsUrl = bsUrl;
    }

    public String getBsComment() {
        return this.bsComment;
    }

    public void setBsComment(String bsComment) {
        this.bsComment = bsComment;
    }

    public boolean isBsSys() {
        return this.bsSys;
    }

    public void setBsSys(boolean bsSys) {
        this.bsSys = bsSys;
    }

    public FsCatalog getBsParent() {
        return this.bsParent;
    }

    public void setBsParent(FsCatalog bsParent) {
        this.bsParent = bsParent;
    }
}
