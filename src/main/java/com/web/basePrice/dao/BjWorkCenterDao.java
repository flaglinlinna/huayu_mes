package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.BjWorkCenter;

import java.util.List;
import java.util.Map;

/**
 *
 * @date Nov 4, 2020 4:50:15 PM
 */
public interface BjWorkCenterDao extends CrudRepository<BjWorkCenter, Long>,JpaSpecificationExecutor<BjWorkCenter>{

    public List<BjWorkCenter> findAll();
    public List<BjWorkCenter> findByDelFlag(Integer delFlag);
    public List<BjWorkCenter> findByWorkcenterNameAndDelFlag(String workcenterName, Integer delFlag);
    int countByDelFlagAndWorkcenterCode(Integer delFlag,String workcenterCode);
    public List<BjWorkCenter> findByDelFlagAndWorkcenterCode(Integer delFlag,String workCenterCode);
    public BjWorkCenter findById(long id);

    @Query(value = "SELECT id,WORKCENTER_NAME FROM BJ_base_WORKCENTER where DEL_FLAG = 0",nativeQuery = true)
    public  List<Map<String, Object>> findIdAndName();
}