package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Defective;
import com.web.basic.entity.Department;
import com.web.basic.entity.Line;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.Process;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备管理（指纹机设备）
 */
@Entity(name = "DevClock")
@Table(name= DevClock.TABLE_NAME)
@DynamicUpdate
public class DevClock extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_BASE_DEV_CLOCK";

    /**
     * 卡机编码
     */
    @ApiModelProperty(name = "devCode", value = "卡机编码")
    @Column(length = 50)
    protected String devCode;

    /**
     * 卡机名称
     */
    @ApiModelProperty(name = "devName", value = "卡机名称")
    @Column(length = 50)
    protected String devName;
    
    /**
     * 卡机IP
     */
    @ApiModelProperty(name = "devIp", value = "卡机IP")
    @Column(length = 50)
    protected String devIp;
    
    /**
     * 卡机序列
     */
    @ApiModelProperty(name = "devSeries", value = "卡机序列")
    @Column(length = 50)
    protected String devSeries;   
    
    
    /**
     * 线体Id
     */
    @ApiModelProperty(name="lineId",value="线别ID")
    @Column
    protected Long lineId;

    @ApiModelProperty(name="line",hidden=true,value="线别ID")
    @ManyToOne
    @JoinColumn(name = "lineId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Line line;
    
    /**
     * 卡机类型
     */
    @ApiModelProperty(name = "devType", value = "卡机类型")
    @Column(length = 50)
    protected String devType;
    
    
    /**
     * 是否在线
     */
    
    @ApiModelProperty(name = "isOnline", value = "是否在线（0：在线/1：离线）")
    @Column
    protected Integer isOnline=1;

    /**
     * 是否有效
     */
    
    @ApiModelProperty(name = "enabled", value = "是否有效（1：有效/0：无效）")
    @Column
    protected Integer enabled=1;


	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getDevIp() {
		return devIp;
	}

	public void setDevIp(String devIp) {
		this.devIp = devIp;
	}

	public String getDevSeries() {
		return devSeries;
	}

	public void setDevSeries(String devSeries) {
		this.devSeries = devSeries;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public Integer getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}
}
