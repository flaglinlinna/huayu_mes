package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import com.system.role.entity.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 工艺关联角色信息管理
 *
 */
@Entity(name = "ProcRole")
@Table(name = ProcRole.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProcRole extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_PROC_ROLE";
	    
	    /**
	     * 角色id
	     */
	    @ApiModelProperty(name = "pkSysRole", value = "角色id")
	    @Column(length = 50)
	    protected Long pkSysRole;

		@ApiModelProperty(name = "sysRole", hidden = true, value = "角色信息")
		@ManyToOne
		@JoinColumn(name = "pkSysRole", insertable = false, updatable = false)
		@NotFound(action = NotFoundAction.IGNORE)
		protected SysRole sysRole;

		/**
		 * wgId
		 */
		@ApiModelProperty(name = "pkProc", value = "工艺ID")
		@Column(length = 50)
		protected Long pkProc;

		@ApiModelProperty(name = "pkProc", hidden = true, value = "工艺信息")
		@ManyToOne
		@JoinColumn(name = "pkProc", insertable = false, updatable = false)
		@NotFound(action = NotFoundAction.IGNORE)
		protected Proc proc;

		public Long getPkSysRole() {
			return pkSysRole;
		}

		public void setPkSysRole(Long pkSysRole) {
			this.pkSysRole = pkSysRole;
		}

		public SysRole getSysRole() {
			return sysRole;
		}

		public void setSysRole(SysRole sysRole) {
			this.sysRole = sysRole;
		}

		public Long getPkProc() {
			return pkProc;
		}

		public void setPkProc(Long pkProc) {
			this.pkProc = pkProc;
		}

		public Proc getProc() {
			return proc;
		}

		public void setProc(Proc proc) {
			this.proc = proc;
		}
}
