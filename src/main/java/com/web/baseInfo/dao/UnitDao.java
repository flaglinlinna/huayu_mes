package com.web.baseInfo.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.baseInfo.entity.Unit;

/**
 * @author hjj
 * @date Nov 4, 2020 4:27:53 PM
 */
public interface UnitDao extends CrudRepository<Unit, Long>,JpaSpecificationExecutor<Unit>{

    public List<Unit> findAll();
    public List<Unit> findByDelFlag(Integer delFlag);
    int countByDelFlagAndUnitCode(Integer delFlag,String unitCode);
    public Unit findById(long id);
}