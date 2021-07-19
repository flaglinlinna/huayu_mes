package com.web.basePrice.dao;

import com.web.basePrice.entity.ItemTypeWgRole;
import com.web.basePrice.entity.ProcRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProcRoleDao extends CrudRepository<ProcRole, Long>,JpaSpecificationExecutor<ProcRole>{

    public List<ProcRole> findAll();
    public List<ProcRole> findByDelFlag(Integer delFlag);
    public List<ProcRole> findByDelFlagAndPkProc(Integer delFlag,Long pkProc);

    @Modifying
    @Query("update ProcRole t set t.delFlag=1 where t.pkProc=?1 and t.delFlag=0")
    public void deleteByPkProc(Long pkProc);

    public ProcRole findById(long id);

}