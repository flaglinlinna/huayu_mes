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
 * 外购物料类型
 *
 */
@Entity(name = "ItemTypeWgRole")
@Table(name = ItemTypeWgRole.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ItemTypeWgRole extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_ITEM_TYPE_WG_ROLE";
	    
	    /**
	     * 角色id
	     */
	    @ApiModelProperty(name = "roleId", value = "角色id")
	    @Column(length = 50)
	    protected Long roleId;

		@ApiModelProperty(name = "sysRole", hidden = true, value = "角色信息")
		@ManyToOne
		@JoinColumn(name = "roleId", insertable = false, updatable = false)
		@NotFound(action = NotFoundAction.IGNORE)
		protected SysRole sysRole;

		/**
		 * wgId
		 */
		@ApiModelProperty(name = "wgId", value = "外购物料类型ID")
		@Column(length = 50)
		protected Long wgId;

		@ApiModelProperty(name = "itemTypeWg", hidden = true, value = "外购物料类型")
		@ManyToOne
		@JoinColumn(name = "wgId", insertable = false, updatable = false)
		@NotFound(action = NotFoundAction.IGNORE)
		protected ItemTypeWg itemTypeWg;

		public Long getRoleId() {
			return roleId;
		}

		public void setRoleId(Long roleId) {
			this.roleId = roleId;
		}

		public Long getWgId() {
			return wgId;
		}

		public void setWgId(Long wgId) {
			this.wgId = wgId;
		}

		public ItemTypeWg getItemTypeWg() {
			return itemTypeWg;
		}

		public void setItemTypeWg(ItemTypeWg itemTypeWg) {
			this.itemTypeWg = itemTypeWg;
		}
}
