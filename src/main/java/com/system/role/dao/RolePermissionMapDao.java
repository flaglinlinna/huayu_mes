package com.system.role.dao;

import com.system.role.entity.RolePermissionMap;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 角色菜单关系表
 */
public interface RolePermissionMapDao extends CrudRepository<RolePermissionMap, Long>, JpaSpecificationExecutor<RolePermissionMap> {

    public List<RolePermissionMap> findByIsDelAndAndRoleId(Integer isDel, Long id);
}
