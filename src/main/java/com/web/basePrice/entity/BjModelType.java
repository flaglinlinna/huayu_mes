package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 机台类型维护
 *
 */
@Entity(name = "BjModelType")
@Table(name = BjModelType.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class BjModelType extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_MODEL_TYPE";

		/**
		 * 机台编码
		 */
		@ApiModelProperty(name = "modelCode", value = "机台编码")
		@Column(length = 20)
		protected String modelCode;

		/**
		 * 机台名称
		 */
		@ApiModelProperty(name = "modelName", value = "机台名称")
		@Column(length = 20)
		protected String modelName;
	    
	    /**
	     * 工作中心Id
	     */
	    @ApiModelProperty(name = "pkWorkcenter", value = "工作中心Id")
	    @Column(length = 20)
	    protected Long pkWorkcenter;
	    
	    @ApiModelProperty(name="workCenter",hidden=true,value="工作中心id")
	    @ManyToOne
	    @JoinColumn(name="workcenterId",insertable=false,updatable=false)
	    @NotFound(action=NotFoundAction.IGNORE)
	    protected BjWorkCenter workCenter;
	    

		/**
	     * 工序Id
	     */
	    @ApiModelProperty(name = "procId", value = "工序Id")
	    @Column(length = 20)
	    protected Long procId;

		public Long getPkWorkcenter() {
			return pkWorkcenter;
		}

		public void setPkWorkcenter(Long pkWorkcenter) {
			this.pkWorkcenter = pkWorkcenter;
		}

		public BjWorkCenter getWorkCenter() {
			return workCenter;
		}

		public void setWorkCenter(BjWorkCenter workCenter) {
			this.workCenter = workCenter;
		}

		public Long getProcId() {
			return procId;
		}

		public void setProcId(Long procId) {
			this.procId = procId;
		}

		public String getModelCode() {
			return modelCode;
		}

		public void setModelCode(String modelCode) {
			this.modelCode = modelCode;
		}

		public String getModelName() {
			return modelName;
		}

		public void setModelName(String modelName) {
			this.modelName = modelName;
		}
}
