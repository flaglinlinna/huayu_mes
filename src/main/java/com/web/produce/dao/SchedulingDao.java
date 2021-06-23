package com.web.produce.dao;

import com.web.produce.entity.Scheduling;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * 排产信息
 */
public interface SchedulingDao extends CrudRepository<Scheduling, Long>, JpaSpecificationExecutor<Scheduling> {

    public Scheduling findById(long id);

    @Query(value = "select f_get_parameter_val ('ORD_IMP_BF_DAY') from dual" , nativeQuery = true)
    public String getBfDay();

    @Query(value = "select f_get_parameter_val ('ORD_IMP_AF_DAY') from dual" , nativeQuery = true)
    public String getAfDay();

}
