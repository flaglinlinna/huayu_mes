package com.system.role.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.system.role.entity.SysRole;

public interface SysRoleDao extends CrudRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    public List<SysRole> findAll();

    public List<SysRole> findByIsDel(Integer isDel);

    public SysRole findById(long id);

    public int countByIsDelAndBsCode(Integer isDel, String bsCode);
}
