package com.system.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

/**
 * 角色基础信息表
 *
 */
@Entity(name = "sysRouterCols")
@Table(name= SysReportCols.TABLE_NAME)
@DynamicUpdate
public class SysReportCols extends BaseEntity {
	private static final long serialVersionUID = -5951531333314901264L;
	public static final String TABLE_NAME = "sys_report_cols";


	/**
	 * 存储过程名称
	 */
	@Column(length=255)
	protected String bsPro;
	
	/**
	 * 是否分页
	 * true/false
	 */
	@Column(length=255)
	protected String bsPage;
	
	/**
	 * 合并行号
	 * 逗号分开
	 */
	@Column(length=255)
	protected String bsRowspanName;
	
	/**
	 * 合并列名称
	 * 逗号分开
	 */
	@Column(length=255)
	protected String bsRowspanIndex;
	
	/**
	 * 是否启用
	 */
	@Column(length=255)
    protected int routerStatus;
	
	/**
	 * 序号
	 */
	@Column(length=255)
    protected int routerIndex;
	
	/**
	 * 父节点Id
	 */
	@Column(length=255)
	protected Long parentId;
	

}
