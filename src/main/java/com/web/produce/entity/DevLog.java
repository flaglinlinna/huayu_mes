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
 * 卡机操作日志表
 */
@Entity(name = "DevLog")
@Table(name= DevLog.TABLE_NAME)
@DynamicUpdate
public class DevLog extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_DEV_OPER_LOG";

    /**
     * 操作员
     */
    @ApiModelProperty(name = "operBy", value = "操作员")
    @Column(length = 50)
    protected String operBy;
    
    /**
     * 操作描述
     */
    @ApiModelProperty(name = "description", value = "操作描述")
    @Column(length = 500)
    protected String description;
    
    /**
     * 操作时间
     */
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@ApiModelProperty(name="timeOper",value="操作时间")
	protected Date timeOper;

	public String getOperBy() {
		return operBy;
	}

	public void setOperBy(String operBy) {
		this.operBy = operBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTimeOper() {
		return timeOper;
	}

	public void setTimeOper(Date timeOper) {
		this.timeOper = timeOper;
	}
}
