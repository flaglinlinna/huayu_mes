package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.Proc;

import java.util.List;

/**
 * @author lst
 * @date Dec 4, 2020 3:14:33 PM
 */
public interface ProcDao extends CrudRepository<Proc, Long>,JpaSpecificationExecutor<Proc>{

    public List<Proc> findAll();
    public List<Proc> findByDelFlag(Integer delFlag);
    int countByDelFlagAndProcNo(Integer delFlag,String procNo);
    public Proc findById(long id);
}