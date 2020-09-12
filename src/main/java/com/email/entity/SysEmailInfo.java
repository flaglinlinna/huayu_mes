package com.email.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 邮件记录
 */
@Entity
@Table(name = SysEmailInfo.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysEmailInfo extends BaseEntity {
    private static final long serialVersionUID = 7151771262953316256L;
    public static final String TABLE_NAME = "sys_email_info";

    /**
     * 发件人地址（多个用“，”隔开）
     */
    @ApiModelProperty(name = "bsEmailFrom", value = "发件人地址（多个用“，”隔开）")
    @Column(length = 500)
    protected String bsEmailFrom;

    /**
     * 收件人地址（多个用“，”隔开）
     */
    @ApiModelProperty(name = "bsEmailTo", value = "收件人地址（多个用“，”隔开）")
    @Column(length = 500)
    protected String bsEmailTo;

    /**
     * 抄送人地址（多个用“，”隔开）
     */
    @ApiModelProperty(name = "bsEmailCc", value = "抄送人地址（多个用“，”隔开）")
    @Column(length = 500)
    protected String bsEmailCc;

    /**
     * 邮件主题
     */
    @ApiModelProperty(name = "bsSubject", value = "邮件主题")
    @Column(length = 100)
    protected String bsSubject;

    /**
     * 邮件内容
     */
    @ApiModelProperty(name = "bsContent", value = "邮件内容")
    @Column(length = 3000)
    protected String bsContent;

    public String getBsEmailFrom() {
        return bsEmailFrom;
    }

    public void setBsEmailFrom(String bsEmailFrom) {
        this.bsEmailFrom = bsEmailFrom;
    }

    public String getBsEmailTo() {
        return bsEmailTo;
    }

    public void setBsEmailTo(String bsEmailTo) {
        this.bsEmailTo = bsEmailTo;
    }

    public String getBsEmailCc() {
        return bsEmailCc;
    }

    public void setBsEmailCc(String bsEmailCc) {
        this.bsEmailCc = bsEmailCc;
    }

    public String getBsSubject() {
        return bsSubject;
    }

    public void setBsSubject(String bsSubject) {
        this.bsSubject = bsSubject;
    }

    public String getBsContent() {
        return bsContent;
    }

    public void setBsContent(String bsContent) {
        this.bsContent = bsContent;
    }
}
