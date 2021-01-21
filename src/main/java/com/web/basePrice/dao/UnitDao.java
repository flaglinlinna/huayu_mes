package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.Unit;

import java.util.List;

/**
 * @author hjj
 * @date Nov 4, 2020 4:27:53 PM
 */
public interface UnitDao extends CrudRepository<Unit, Long>,JpaSpecificationExecutor<Unit>{

    public List<Unit> findAll();
    public List<Unit> findByDelFlag(Integer delFlag);
    public List<Unit> findByUnitNameAndDelFlag(String unitName,Integer delFlag);
    public List<Unit> findByUnitCodeAndDelFlag(String unitCode,Integer delFlag);
    int countByDelFlagAndUnitCode(Integer delFlag,String unitCode);
    public Unit findById(long id);
}