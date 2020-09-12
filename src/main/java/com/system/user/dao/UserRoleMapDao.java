package com.system.user.dao;

import com.system.user.entity.UserRoleMap;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 用户角色关系表
 */
public interface UserRoleMapDao extends CrudRepository<UserRoleMap, Long>, JpaSpecificationExecutor<UserRoleMap> {

    public List<UserRoleMap> findByUserId(Long userId);

    public List<UserRoleMap> findByIsDelAndUserId(Integer isDel, Long userId);
}
