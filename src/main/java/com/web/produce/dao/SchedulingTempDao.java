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
    @Query("update SchedulingTemp t set t.isDel=?1 where t.pkSysUser=?2 and t.isDel=0")
    public void updateIsDelByPkSysUser(Integer isDel, Long pkSysUser);

    public List<SchedulingTemp> findByIsDelAndPkSysUser(Integer isDel, Long pkSysUser);
}
