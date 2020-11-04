package com.web.baseInfo.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.baseInfo.entity.BjWorkCenter;

/**
 *
 * @date Nov 4, 2020 4:50:15 PM
 */
public interface BjWorkCenterDao extends CrudRepository<BjWorkCenter, Long>,JpaSpecificationExecutor<BjWorkCenter>{

    public List<BjWorkCenter> findAll();
    public List<BjWorkCenter> findByDelFlag(Integer delFlag);
    int countByDelFlagAndWorkcenterCode(Integer delFlag,String workcenterCode);
    public BjWorkCenter findById(long id);
}