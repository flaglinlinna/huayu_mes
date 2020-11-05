package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 系统参数维护
 *
 */
@Entity(name = "SysParam")
@Table(name = SysParam.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysParam extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "SYS_PARAMETER";
	    
	    /**
	     * 系统参数类型|0-系统级，1-用户级，系统级不允许修改
	     */
	    @ApiModelProperty(name = "paramType", value = "系统参数类型")
	    protected Long paramType;

	    /**
	     * 系统参数种类|业务分类
	     */
	    @ApiModelProperty(name = "paramSort", value = "系统参数种类")
	    @Column(length = 100)
	    protected String paramSort;
	    
	    /**
	     * 参数编码
	     */
	    @ApiModelProperty(name = "paramCode", value = "参数编码")
	    @Column(length = 100)
	    protected String paramCode;
	    
	    /**
	     * 参数名称
	     */
	    @ApiModelProperty(name = "paramName", value = "参数名称")
	    @Column(length = 100)
	    protected String paramName;
	    
	    /**
	     * 数据类型|String/Int/Float/Date/Time/DATE/Boolean
	     */
	    @ApiModelProperty(name = "dataType", value = "数据类型")
	    @Column(length = 20)
	    protected String dataType;
	    
	    /**
	     * 参数值
	     */
	    @ApiModelProperty(name = "paramValue", value = "参数值")
	    @Column(length = 100)
	    protected String paramValue;

		public Long getParamType() {
			return paramType;
		}

		public void setParamType(Long paramType) {
			this.paramType = paramType;
		}

		public String getParamSort() {
			return paramSort;
		}

		public void setParamSort(String paramSort) {
			this.paramSort = paramSort;
		}

		public String getParamCode() {
			return paramCode;
		}

		public void setParamCode(String paramCode) {
			this.paramCode = paramCode;
		}

		public String getParamName() {
			return paramName;
		}

		public void setParamName(String paramName) {
			this.paramName = paramName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getParamValue() {
			return paramValue;
		}

		public void setParamValue(String paramValue) {
			this.paramValue = paramValue;
		}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("系统参数类型:").append(this.paramType);
		sb.append(",系统参数种类:").append(this.paramSort);
		sb.append(",参数编码:").append(this.paramCode);
		sb.append(",参数名称:").append(this.paramName);
		sb.append(",数据类型:").append(this.dataType);
		sb.append(",参数值:").append(this.paramValue);
		sb.append("\n]");
		return sb.toString();
	}
}
