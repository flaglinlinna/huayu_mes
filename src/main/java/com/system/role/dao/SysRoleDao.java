package com.system.role.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.system.role.entity.SysRole;

public interface SysRoleDao extends CrudRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    public List<SysRole> findAll();

    public List<SysRole> findByDelFlag(Integer delFlag);
    
    
    public List<SysRole> findByDelFlagAndStatus(Integer delFlag,Integer status);

    public SysRole findById(long id);

    public int countByDelFlagAndRoleCode(Integer delFlag, String roleCode);
    
    @Query(value = "select t from SysRole t left join UserRoleMap m on m.roleId = t.id and m.userId =:userId where m.delFlag = 0 and t.status=0 and t.delFlag = 0")
	public List<SysRole> getRoleByUser(@Param("userId") Long userId);
}
