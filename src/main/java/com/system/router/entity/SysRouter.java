package com.system.router.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

/**
 * 角色基础信息表
 *
 */
@Entity(name = "sysRouter")
@Table(name= SysRouter.TABLE_NAME)
@DynamicUpdate
public class SysRouter extends BaseEntity {
	private static final long serialVersionUID = -5951531333314901264L;
	public static final String TABLE_NAME = "sys_router";


	/**
	 * 备注
	 */
	@Column(length=255)
	protected String routerComment;
	
	/**
	 * 菜单名
	 */
	@Column(length=255)
	protected String routerName;
	
	/**
	 * 菜单代码
	 */
	@Column(length=255)
	protected String routerCode;
	
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
	

	public String getRouterComment() {
		return routerComment;
	}

	public void setRouterComment(String routerComment) {
		this.routerComment = routerComment;
	}

	public String getRouterName() {
		return routerName;
	}

	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	public String getRouterCode() {
		return routerCode;
	}

	public void setRouterCode(String routerCode) {
		this.routerCode = routerCode;
	}

    public int getRouterStatus() {
        return routerStatus;
    }

    public void setRouterStatus(int routerStatus) {
        this.routerStatus = routerStatus;
    }

    public int getRouterIndex() {
        return routerIndex;
    }

    public void setRouterIndex(int routerIndex) {
        this.routerIndex = routerIndex;
    }

    public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	

}
