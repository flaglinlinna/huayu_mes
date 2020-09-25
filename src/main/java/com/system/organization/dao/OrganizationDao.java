package com.system.organization.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.organization.entity.SysOrganization;
import com.system.permission.entity.SysPermission;
import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserRoleMap;

/**
 * 菜单基础信息表
 */
public interface OrganizationDao extends CrudRepository<SysOrganization, Long>, JpaSpecificationExecutor<SysOrganization> {
	
	public List<SysOrganization> findByDelFlag(Integer delFlag);
	
	public List<SysOrganization> findByDelFlagAndParentId(Integer delFlag,long pid);
	
	public SysOrganization findByIdAndDelFlag(long id,Integer delFlag);
	
//	@Query(value = "select "+
//				   "  p.id, p.bs_name,p.parent_id pId, p.zindex, p.istype, p.bs_code, p.icon, p.page_url "+
//				   " from permission p "+
//				   " LEFT JOIN role_permission rp ON rp.permit_id=p.id "+
//				   " LEFT JOIN role r ON r.id=rp.role_id "+
//				   " LEFT JOIN user_role ur ON ur.role_id=r.id "+
//				   " WHERE ur.user_id=?1 and p.is_del=0 "+
//				   " GROUP BY p.id "+
//				   " order by p.zindex ", nativeQuery = true)
//	  public List<Map<String, Object>> getUserPerms(long id);
    @Query(value = "select "+
            "  p.id, p.menu_name,p.parent_id pId, p.zindex, p.istype, p.menu_code, p.menu_icon, p.page_url "+
            " from "+SysPermission.TABLE_NAME+" p "+
           " LEFT JOIN "+RolePermissionMap.TABLE_NAME+" rp ON rp.permit_id=p.id "+
            " LEFT JOIN "+SysRole.TABLE_NAME +" r ON r.id=rp.role_id "+
           " LEFT JOIN "+UserRoleMap.TABLE_NAME+" ur ON ur.role_id=r.id "+
           " WHERE ur.user_id=?1 and p.del_flag=0 and rp.del_flag=0 and ur.del_flag=0"+
           " order by p.zindex ", nativeQuery = true)
    public List<Map<String, Object>> getUserPerms(long id);
}
