package com.app.base.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.system.user.entity.SysUser;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AuditEntity extends BaseEntity {
	private static final long serialVersionUID = -6784498964459870355L;

	/**
	 * 创建人
	 */
	@Column
	protected Long pkCreatedBy;
	/**
	 * 修改人
	 */
	@Column
    protected Long pkModifiedBy;
	/**
	 * 所属组织
	 */
	@Column
	protected Long pkGroup;
	/**
	 * 所属组织
	 */
	@Column
	protected Long pkOrg;

	@ManyToOne
	@JoinColumn(name="pkCreatedBy", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	protected SysUser createdBy;

	@ManyToOne
	@JoinColumn(name="pkModifiedBy", insertable=false, updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	protected SysUser modifiedBy;

	public Long getPkCreatedBy() {
		return pkCreatedBy;
	}
	public void setPkCreatedBy(Long pkCreatedBy) {
		this.pkCreatedBy = pkCreatedBy;
	}
	public Long getPkModifiedBy() {
		return pkModifiedBy;
	}
	public void setPkModifiedBy(Long pkModifiedBy) {
		this.pkModifiedBy = pkModifiedBy;
	}
	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}
	public void setModifiedBy(SysUser modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Long getPkGroup() {
		return pkGroup;
	}
	public void setPkGroup(Long pkGroup) {
		this.pkGroup = pkGroup;
	}
	public Long getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(Long pkOrg) {
		this.pkOrg = pkOrg;
	}
}
