package com.web.produce.dao;

import com.web.produce.entity.SchedulingDet;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 排产信息(导入制令单) 新 从表 2020/11/12
 */
public interface SchedulingDetDao extends CrudRepository<SchedulingDet, Long>, JpaSpecificationExecutor<SchedulingDet> {

    public SchedulingDet findById(long id);
}
