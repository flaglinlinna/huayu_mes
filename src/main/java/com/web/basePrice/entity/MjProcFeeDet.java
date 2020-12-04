package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 模具成本导入-从表
 *
 */
@Entity(name = "MjProcFeeDet")
@Table(name = MjProcFeeDet.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class MjProcFeeDet extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_MJ_PROC_FEE_DET";
	    
	    /**
	     * 主表ID
	     */
	    @ApiModelProperty(name = "mid", value = "主表ID")
	    @Column(length = 20)
	    protected Long mid;

	    /**
	     * 工序名称
	     */
	    @ApiModelProperty(name = "procName", value = "工序名称")
	    @Column(length = 50)
	    protected String procName;

		/**
		 * 工序时间
		 */
		@ApiModelProperty(name="ftimeUse",value="工序时间")
		@Column(length = 50)
		protected String ftimeUse;

		public Long getMid() {
			return mid;
		}

		public void setMid(Long mid) {
			this.mid = mid;
		}

		public String getProcName() {
			return procName;
		}

		public void setProcName(String procName) {
			this.procName = procName;
		}

		public String getFtimeUse() {
			return ftimeUse;
		}

		public void setFtimeUse(String ftimeUse) {
			this.ftimeUse = ftimeUse;
		}
}
