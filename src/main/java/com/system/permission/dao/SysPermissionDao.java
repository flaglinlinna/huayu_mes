package com.system.permission.dao;

import java.util.List;
import java.util.Map;

import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserRoleMap;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.permission.entity.SysPermission;

/**
 * 菜单基础信息表
 */
public interface SysPermissionDao extends CrudRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {
	
	public List<SysPermission> findByIsDel(Integer isDel);
	
	public List<SysPermission> findByIsDelAndParentId(Integer isDel,long pid);
	
	public SysPermission findByIdAndIsDel(long id,Integer isDel);
	
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
    @Query(value = "select distinct "+
            "  p.id ID, p.BS_NAME,p.parent_id PID, p.ZINDEX , p.ISTYPE, p.BS_CODE, p.BS_ICON, p.PAGE_URL "+
            " from "+SysPermission.TABLE_NAME+" p "+
           " LEFT JOIN "+RolePermissionMap.TABLE_NAME+" rp ON rp.permit_id=p.id "+
            " LEFT JOIN "+SysRole.TABLE_NAME +" r ON r.id=rp.role_id "+
           " LEFT JOIN "+UserRoleMap.TABLE_NAME+" ur ON ur.role_id=r.id "+
           " WHERE ur.user_id=?1 and p.is_del=0 and rp.is_del=0 and ur.is_del=0"+
           " order by p.zindex ", nativeQuery = true)
    public List<Map<String, Object>> getUserPerms(long id);
}
