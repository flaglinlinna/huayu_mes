package com.app.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * 增加编码、名称字段
 * @author tanxiang
 *
 */
@MappedSuperclass
public abstract class CommonEntity extends AuditEntity {
	private static final long serialVersionUID = -8557069300645817687L;

	/**
	 * 编码
	 */
	@NotNull
	@Column(length = 200)
	protected String bsCode;
	/**
	 * 名称
	 */
	@NotNull
	@Column(length = 100)
	protected String bsName;

	public String getBsCode() {
		return bsCode;
	}
	public void setBsCode(String bsCode) {
		this.bsCode = bsCode;
	}
	public String getBsName() {
		return bsName;
	}
	public void setBsName(String bsName) {
		this.bsName = bsName;
	}
}
