package com.web.basic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * APP版本
 *
 */
@Entity(name = "AppVersion")
@Table(name = AppVersion.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class AppVersion extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "MES_app_version";


    /**
     * 版本号
     */
    @ApiModelProperty(name = "versionNo", value = "版本号")
    @Column(length = 50)
    protected String versionNo;

    /**
     * 地址
     */
    @ApiModelProperty(name = "versionUrl", value = "地址")
    @Column(length = 50)
    protected String versionUrl;
    


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("版本号:").append(this.versionNo);
        sb.append(",地址:").append(this.versionUrl);
        return sb.toString();
    }



	public String getVersionNo() {
		return versionNo;
	}



	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}



	public String getVersionUrl() {
		return versionUrl;
	}



	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}
    
}
