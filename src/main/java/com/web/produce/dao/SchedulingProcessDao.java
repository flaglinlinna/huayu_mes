package com.web.produce.dao;

import com.web.produce.entity.SchedulingProcess;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 排产信息-生产制令单从表-工艺
 */
public interface SchedulingProcessDao extends CrudRepository<SchedulingProcess, Long>, JpaSpecificationExecutor<SchedulingProcess> {

    public SchedulingProcess findById(long id);
}
