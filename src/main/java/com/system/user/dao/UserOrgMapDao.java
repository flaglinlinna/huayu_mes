package com.system.user.dao;

import com.system.permission.entity.SysPermission;
import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserOrgMap;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

/**
 * 用户角色关系表
 */
public interface UserOrgMapDao extends CrudRepository<UserOrgMap, Long>, JpaSpecificationExecutor<UserOrgMap> {

    public List<UserOrgMap> findByUserId(Long userId);

//    @Query(value = )
    Integer countByOrgIdAndAndDelFlag(Long orgId,int delFlag);
    
    Integer countByUserIdAndAndDelFlag(Long userId,int delFlag);

    public List<UserOrgMap> findByDelFlagAndUserId(Integer delFlag, Long userId);
    
    public List<UserOrgMap> findByDelFlagAndOrgId(Integer delFlag, Long orgId);
    
//    @Query(value = "select count(u.id)c from  sys_user u left join  sys_user_role r on r.user_id=u.id  where u.del_flag=0 and u.status=0 and r.role_id=?1 and r.del_flag=0 ", nativeQuery = true)
//    public List<Map<String, Object>> getUserByOrgId(Long orgId);

   
//    @Query(value = "select r.org_id from sys_user_org r where r.del_flag=0 and r.user_id=?1", nativeQuery = true)
//    public List<Map<String, Object>> getOrgIdByUserId(Long userId);


//    @Query(value = "select u.* from  sys_user u left join  sys_user_role r on r.user_id=u.id  where u.del_flag=0 and u.status=0 and r.role_id=?1 and r.del_flag=0 ", nativeQuery = true)
//    public List<Map<String, Object>> getAllUserByOrgId(Long orgId);

}
