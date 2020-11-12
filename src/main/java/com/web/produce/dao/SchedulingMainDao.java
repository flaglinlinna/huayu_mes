package com.web.produce.dao;


import com.web.produce.entity.SchedulingMain;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 排产信息
 */
public interface SchedulingMainDao extends CrudRepository<SchedulingMain, Long>, JpaSpecificationExecutor<SchedulingMain> {

    public SchedulingMain findById(long id);
}
