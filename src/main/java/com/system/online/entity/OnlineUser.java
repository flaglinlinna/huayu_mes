package com.system.online.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * 在线用户表
 *
 */
public class OnlineUser  {

	@ApiModelProperty(name = "id", value = "用户session Id")
	protected Long id;
    /**
     * 用户session Id
     */
    @ApiModelProperty(name = "sessionId", value = "用户session Id")
    @Column(length = 500)
    protected String sessionId;
    
    /**
     * 用户账号
     */
    @ApiModelProperty(name = "bsCode", value = "用户账号")
    @Column(length = 50)
    protected String bsCode;

    /**
     * 用户名称
     */
    @ApiModelProperty(name = "bsName", value = "用户名称")
    @Column(length = 50)
    protected String bsName;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "mobile", value = "手机号")
    @Column(length = 15)
    protected String mobile;

    /**
     * 登录IP
     */
    @ApiModelProperty(name = "host", value = "登录IP")
    @Column(length = 10)
    protected String host;

    /**
     * 最后操作时间
     */
    @ApiModelProperty(name = "lastAccessTime", value = "最后操作时间")
    @Column
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    protected Date lastAccessTime;
    
    /**
     * 登录时间
     */
    @ApiModelProperty(name = "startTimestamp", value = "登录时间")
    @Column
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    protected Date startTimestamp;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

}
