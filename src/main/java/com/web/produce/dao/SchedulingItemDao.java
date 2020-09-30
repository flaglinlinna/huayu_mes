package com.web.produce.dao;

import com.web.produce.entity.SchedulingItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 排产信息-生产制令单从表-组件
 */
public interface SchedulingItemDao extends CrudRepository<SchedulingItem, Long>, JpaSpecificationExecutor<SchedulingItem> {

    public SchedulingItem findById(long id);
}
