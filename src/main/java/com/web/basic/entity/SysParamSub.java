package com.web.basic.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统参数维护
 *
 */
@Entity(name = "SysParamSub")
@Table(name = SysParamSub.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysParamSub extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "SYS_PARAMETER_SUB1";
	    
	    /**
	     * 主表id
	     */
	    @ApiModelProperty(name = "mid", value = "主表id")
	    protected Long mid;

	    /**
	     * 快码编码
	     */
	    @ApiModelProperty(name = "subCode", value = "快码编码")
	    @Column(length = 100)
	    protected String subCode;
	    
	    /**
	     * 快码名称
	     */
	    @ApiModelProperty(name = "paramCode", value = "快码名称")
	    @Column(length = 100)
	    protected String subName;
	    
	    /**
	     * 顺序号
	     */
	    @ApiModelProperty(name = "forder", value = "顺序号")
	    @Column(length = 100)
	    protected String forder;

		public Long getMid() {
			return mid;
		}

		public void setMid(Long mid) {
			this.mid = mid;
		}

		public String getSubCode() {
			return subCode;
		}

		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}

		public String getSubName() {
			return subName;
		}

		public void setSubName(String subName) {
			this.subName = subName;
		}

		public String getForder() {
			return forder;
		}

		public void setForder(String forder) {
			this.forder = forder;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer();
			sb.append("主表Id:").append(this.mid);
			sb.append(",快码编码:").append(this.subCode);
			sb.append(",快码名称:").append(this.subName);
			sb.append(",顺序号:").append(this.forder);
			return sb.toString();
		}
}
