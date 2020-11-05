package com.system.user.dao;

import com.system.user.entity.UserRoleMap;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 用户角色关系表
 */
public interface UserRoleMapDao extends CrudRepository<UserRoleMap, Long>, JpaSpecificationExecutor<UserRoleMap> {

    public List<UserRoleMap> findByUserId(Long userId);

//    @Query(value = )
    Integer countByRoleIdAndAndDelFlag(Long roleId,int delFlag);

    public List<UserRoleMap> findByDelFlagAndUserId(Integer delFlag, Long userId);
}
