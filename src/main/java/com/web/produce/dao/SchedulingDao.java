package com.web.produce.dao;

import com.web.produce.entity.Scheduling;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 排产信息
 */
public interface SchedulingDao extends CrudRepository<Scheduling, Long>, JpaSpecificationExecutor<Scheduling> {

    public Scheduling findById(long id);
}
