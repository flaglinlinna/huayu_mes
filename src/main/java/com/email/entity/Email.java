package com.email.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name= Email.TABLE_NAME)
@DynamicUpdate
public class Email extends BaseEntity {
	private static final long serialVersionUID = -5951531333314901264L;
	public static final String TABLE_NAME = "sys_email";

	@ApiModelProperty(name="email_from",value="发件人邮箱")
	@Column(length=200)
	protected String email_from;
	
	@ApiModelProperty(name="email_from",value="收件人邮箱")
	@Column(length=500)
	protected String[] email_to;
	
	@ApiModelProperty(name="email_cc",value="抄送人邮箱")
	@Column(length=500)
	protected String[] email_cc;

	@ApiModelProperty(name="email_subject",value=" 邮件主题")
	@Column(length=500)
	protected String email_subject;
	
	@ApiModelProperty(name="email_header",value=" 邮件标题")
	@Column(length=255)
	protected String email_header;
	
	@ApiModelProperty(name="sentDate",value="发送时间")
    @Column
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	protected Date sentDate;
	
	@ApiModelProperty(name="email_content",value=" 邮件内容")
	@Column(length=500)
	protected String email_content;
	
	@ApiModelProperty(name="email_Files",value=" 邮件附件")
	@Column(length=500)
	protected String[] email_Files;

	public String getEmail_from() {
		return email_from;
	}

	public void setEmail_from(String email_from) {
		this.email_from = email_from;
	}

	public String[] getEmail_to() {
		return email_to;
	}

	public void setEmail_to(String[] email_to) {
		this.email_to = email_to;
	}

	public String[] getEmail_cc() {
		return email_cc;
	}

	public void setEmail_cc(String[] email_cc) {
		this.email_cc = email_cc;
	}

	public String getEmail_subject() {
		return email_subject;
	}

	public void setEmail_subject(String email_subject) {
		this.email_subject = email_subject;
	}

	public String getEmail_header() {
		return email_header;
	}

	public void setEmail_header(String email_header) {
		this.email_header = email_header;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getEmail_content() {
		return email_content;
	}

	public void setEmail_content(String email_content) {
		this.email_content = email_content;
	}

	public String[] getEmail_Files() {
		return email_Files;
	}

	public void setEmail_Files(String[] email_Files) {
		this.email_Files = email_Files;
	}
	
	

}
