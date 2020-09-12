package com.system.router.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.router.entity.SysRouter;

public interface SysRouterDao extends  CrudRepository<SysRouter, Long>, JpaSpecificationExecutor<SysRouter>  {


    public List<SysRouter> findByIsDelAndRouterStatusOrderByRouterIndexAsc(Integer isDel,int status);
    
    @Query(value = "select router_code from sys_router r where r.id in (select router_id from t_router_roles_map m where m.role_Code in (select role_Code from t_user_roles_map u where u.user_id=?1))", nativeQuery = true)
	public List<String> getRolesByUserId(long userId);

    public SysRouter findById(long id);
//    @Query(value = "select roleCode from userRolesMap where userId= ?1")
//	public List<String> getRolesByUserId(long uid);
//    
//    @Transactional
//    @Modifying
//	@Query(value = "delete userRolesMap where userId = ?1")
//	public void deleteRolesByUserId(long userId);
    
    @Query(value = "select a.* from "+
"(select DISTINCT s.id,s.parent_id,s.router_code,s.router_name,s.router_index ,case  WHEN m.router_id IS NULL then 0 WHEN m.router_id IS NOT NULL then m.router_id END router_id from sys_router s LEFT JOIN t_router_roles_map m ON s.id = m.router_id and m.role_code=?1 "+
"where s.is_del=0 and s.router_status=0 )A ORDER BY a.router_index", nativeQuery = true)
	public List<Map<String, Object>> getRouterTree(String roleCode);

}
