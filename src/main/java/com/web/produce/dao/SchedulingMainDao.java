package com.web.produce.dao;


import com.web.produce.entity.SchedulingMain;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * 排产信息
 */
public interface SchedulingMainDao extends CrudRepository<SchedulingMain, Long>, JpaSpecificationExecutor<SchedulingMain> {

    public SchedulingMain findById(long id);

    @Query(value = "select F_GetBillCode(?1) from dual" , nativeQuery = true)
    public String  getBillCode(Integer billCode);
}
