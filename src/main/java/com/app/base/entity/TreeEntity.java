package com.app.base.entity;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class TreeEntity<T extends Serializable> extends CommonEntity implements Tree<T> {
	private static final long serialVersionUID = -1119026749738515599L;

	protected T pkParent;


	/**
	 * 资源图标class
	 */
	@Column(length = 100)
	protected String bsIconCls;

	/**
	 * 节点层级
	 */
	@Column
	protected Integer bsLevel;

	/**
	 * 是否子节点
	 */
	@Column
	protected Integer bsLeaf = 1;

	/**
	 * 顺序号
	 */
	@Column
	protected Integer bsSortNo = 0;

	public T getPkParent() {
		return pkParent;
	}
	public void setPkParent(T pkParent) {
		this.pkParent = pkParent;
	}
	public String getBsIconCls() {
		return bsIconCls;
	}
	public void setBsIconCls(String bsIconCls) {
		this.bsIconCls = bsIconCls;
	}
	public Integer getBsLevel() {
		return bsLevel;
	}
	public void setBsLevel(Integer bsLevel) {
		this.bsLevel = bsLevel;
	}
	public Integer getBsLeaf() {
		return bsLeaf;
	}
	public void setBsLeaf(Integer bsLeaf) {
		this.bsLeaf = bsLeaf;
	}
	public Integer getBsSortNo() {
		return bsSortNo;
	}
	public void setBsSortNo(Integer bsSortNo) {
		this.bsSortNo = bsSortNo;
	}
	public T getParent() {
		return this.pkParent;
	}
	public Integer getLevel() {
		return this.bsLevel;
	}
	public Tree<T> getAttributes() {
		return null;
	}
	public String getText() {
		return this.bsName;
	}
	public String getIconCls() {
		return this.bsIconCls;
	}

}
