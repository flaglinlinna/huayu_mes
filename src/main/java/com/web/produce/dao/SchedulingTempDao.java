package com.web.produce.dao;

import com.web.produce.entity.SchedulingTemp;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 排产信息导入临时表
 */
public interface SchedulingTempDao extends CrudRepository<SchedulingTemp, Long>, JpaSpecificationExecutor<SchedulingTemp> {

    public SchedulingTemp findById(long id);

    @Modifying
    @Query("update SchedulingTemp t set t.delFlag=?1 where t.createBy=?2 and t.delFlag=0")
    public void updateDelFlagByCreateBy(Integer delFlag, Long createBy);

    public List<SchedulingTemp> findByDelFlagAndCreateBy(Integer delFlag, Long createBy);

    public int countByDelFlagAndCreateBy(Integer delFlag, Long createBy);
}
