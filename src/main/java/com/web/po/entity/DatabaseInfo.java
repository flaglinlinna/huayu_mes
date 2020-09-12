package com.web.po.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

/**
 * 数据库接口信息配置表
 *
 */
@Entity(name = "databaseInfo")
@Table(name= DatabaseInfo.TABLE_NAME)
@DynamicUpdate
public class DatabaseInfo extends BaseEntity {
	private static final long serialVersionUID = -5951531333314901264L;
	public static final String TABLE_NAME = "api_database_Info";

	/**
	 * 备注
	 */
	@Column(length=255)
	protected String bsComment;
	
	/**
	 * 接口名
	 */
	@Column(length=255)
	protected String bsName;
	
	/**
	 * 接口代码
	 */
	@Column(length=255)
	protected String bsCode;
	
	/**
	 * 是否启用
	 * 1:启用；0:不启用
	 */
	@Column(length=255)
    protected Integer bsStatus=1;
	
	/**
	 * 执行方式
	 * 1:手工触发；2:
	 */
	@Column(length=255)
    protected Integer bsWay=1;
	
	/**
	 * 序号
	 */
	@Column(length=255)
    protected int bsIndex;
	
	/**
	 * 源数据-数据库类型
	 */
	@Column(length=255)
    protected String bsFromType;
	
	/**
	 * 源数据-数据库地址
	 */
	@Column(length=255)
    protected String bsFromUrl;
	
	/**
	 * 源数据-数据库账号
	 */
	@Column(length=255)
    protected String bsFromName;
	
	/**
	 * 源数据-数据库密码
	 */
	@Column(length=255)
    protected String bsFromPassword;
	
	/**
	 * 源数据-主键
	 */
	@Column(length=255)
    protected String bsFromKey;
	
	/**
	 * 源数据-sql
	 */
	@Column(length=255)
    protected String bsFromSql;
	
	/**
	 * 目标数据-数据库类型
	 */
	@Column(length=255)
    protected String bsToType;
	
	/**
	 * 目标数据-数据库地址
	 */
	@Column(length=1000)
    protected String bsToUrl;
	
	/**
	 * 目标数据-数据库账号
	 */
	@Column(length=255)
    protected String bsToName;
	
	/**
	 * 目标数据-数据库密码
	 */
	@Column(length=255)
    protected String bsToPassword;
	
	/**
	 * 目标数据-表名
	 */
	@Column(length=255)
    protected String bsToTable;
	
	/**
	 * 目标数据-主键
	 */
	@Column(length=255)
    protected String bsToKey;
	
	/**
	 * 目标数据-字段
	 */
	@Column(length=1000)
    protected String bsToField;
	
	
	
	public String getBsComment() {
		return bsComment;
	}

	public void setBsComment(String bsComment) {
		this.bsComment = bsComment;
	}

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public String getBsCode() {
		return bsCode;
	}

	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}

	
	

	public Integer getBsStatus() {
		return bsStatus;
	}

	public int getBsIndex() {
		return bsIndex;
	}

	public void setBsIndex(int bsIndex) {
		this.bsIndex = bsIndex;
	}

	public void setBsStatus(Integer bsStatus) {
		this.bsStatus = bsStatus;
	}

	public String getBsFromType() {
		return bsFromType;
	}

	public void setBsFromType(String bsFromType) {
		this.bsFromType = bsFromType;
	}

	public String getBsFromUrl() {
		return bsFromUrl;
	}

	public void setBsFromUrl(String bsFromUrl) {
		this.bsFromUrl = bsFromUrl;
	}

	public String getBsFromKey() {
		return bsFromKey;
	}

	public void setBsFromKey(String bsFromKey) {
		this.bsFromKey = bsFromKey;
	}

	public String getBsFromSql() {
		return bsFromSql;
	}

	public void setBsFromSql(String bsFromSql) {
		this.bsFromSql = bsFromSql;
	}

	public String getBsToType() {
		return bsToType;
	}

	public void setBsToType(String bsToType) {
		this.bsToType = bsToType;
	}

	public String getBsToUrl() {
		return bsToUrl;
	}

	public void setBsToUrl(String bsToUrl) {
		this.bsToUrl = bsToUrl;
	}

	public String getBsToTable() {
		return bsToTable;
	}

	public void setBsToTable(String bsToTable) {
		this.bsToTable = bsToTable;
	}

	public String getBsToKey() {
		return bsToKey;
	}

	public void setBsToKey(String bsToKey) {
		this.bsToKey = bsToKey;
	}

	public String getBsToField() {
		return bsToField;
	}

	public void setBsToField(String bsToField) {
		this.bsToField = bsToField;
	}

	public String getBsFromName() {
		return bsFromName;
	}

	public void setBsFromName(String bsFromName) {
		this.bsFromName = bsFromName;
	}

	public String getBsFromPassword() {
		return bsFromPassword;
	}

	public void setBsFromPassword(String bsFromPassword) {
		this.bsFromPassword = bsFromPassword;
	}

	public String getBsToName() {
		return bsToName;
	}

	public void setBsToName(String bsToName) {
		this.bsToName = bsToName;
	}

	public String getBsToPassword() {
		return bsToPassword;
	}

	public void setBsToPassword(String bsToPassword) {
		this.bsToPassword = bsToPassword;
	}

	public Integer getBsWay() {
		return bsWay;
	}

	public void setBsWay(Integer bsWay) {
		this.bsWay = bsWay;
	}
	
	

	

}
