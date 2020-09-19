package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 产品工艺流程信息表(主表）
 *
 */
@Entity(name = "ProdProc")
@Table(name = ProdProc.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProdProc extends BaseEntity {
  private static final long serialVersionUID = 4625660587007894370L;
     public static final String TABLE_NAME = "basic_prodproc";
     
     /**
      * 客户
      */
     @ApiModelProperty(name = "bsClient", value = "客户")
     @Column(length = 50)
     protected String bsClient;
     
     /**
      * 标准人力
      */
     @ApiModelProperty(name = "bsManper", value = "标准人力")
     @Column(length = 15)
     protected String bsManper;
     
     /**
      * 关联物料表的ID
      */
     @ApiModelProperty(name="bsMtrialId",value="物料ID")
     @Column
     protected Long bsMtrialId;

     @ApiModelProperty(name="mtrial",hidden=true,value="物料信息")
     @ManyToOne
     @JoinColumn(name = "bsMtrialId", insertable = false, updatable = false)
     @NotFound(action = NotFoundAction.IGNORE)
     protected Mtrial mtrial;
     
     /**
      * 关联工时表的ID
      */
     @ApiModelProperty(name="bsWoHoursId",value="工时信息")
     @Column
     protected Long bsWoHoursId;

     @ApiModelProperty(name="woHours",hidden=true,value="工时信息")
     @ManyToOne
     @JoinColumn(name = "bsWoHoursId", insertable = false, updatable = false)
     @NotFound(action = NotFoundAction.IGNORE)
     protected Process woHours;

	public String getBsClient() {
		return bsClient;
	}

	public void setBsClient(String bsClient) {
		this.bsClient = bsClient;
	}

	public String getBsManper() {
		return bsManper;
	}

	public void setBsManper(String bsManper) {
		this.bsManper = bsManper;
	}

	public Long getBsMtrialId() {
		return bsMtrialId;
	}

	public void setBsMtrialId(Long bsMtrialId) {
		this.bsMtrialId = bsMtrialId;
	}

	public Long getBsWoHoursId() {
		return bsWoHoursId;
	}

	public void setBsWoHoursId(Long bsWoHoursId) {
		this.bsWoHoursId = bsWoHoursId;
	}
     
     
}