package com.system.user.dao;

import com.system.permission.entity.SysPermission;
import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserRoleMap;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

/**
 * 用户角色关系表
 */
public interface UserRoleMapDao extends CrudRepository<UserRoleMap, Long>, JpaSpecificationExecutor<UserRoleMap> {

    public List<UserRoleMap> findByUserId(Long userId);

//    @Query(value = )
    Integer countByRoleIdAndAndDelFlag(Long roleId,int delFlag);

    public List<UserRoleMap> findByDelFlagAndUserId(Integer delFlag, Long userId);
    
    @Query(value = "select count(u.id)c from  sys_user u left join  sys_user_role r on r.user_id=u.id  where u.del_flag=0 and u.status=0 and r.role_id=?1 and r.del_flag=0 ", nativeQuery = true)
    public List<Map<String, Object>> getUserByRoleId(Long roleId);
}
