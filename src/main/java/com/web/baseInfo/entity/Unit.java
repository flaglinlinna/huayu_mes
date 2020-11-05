package com.web.baseInfo.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * 基本单位表
 *
 */
@Entity(name = "Unit")
@Table(name = Unit.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Unit extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "MES_BASE_UNIT";
	    
	    /**
	     * 单位编码
	     */
	    @ApiModelProperty(name = "unitCode", value = "单位编码")
	    @Column(length = 50)
	    protected String unitCode;

	    /**
	     * 单位名称
	     */
	    @ApiModelProperty(name = "unitName", value = "单位名称")
	    @Column(length = 50)
	    protected String unitName;




	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
