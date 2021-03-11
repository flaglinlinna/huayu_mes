package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.BaseFee;

import java.util.List;

/**
 * @author lst
 * @date Dec 8, 2020 11:24:36 AM
 */
public interface BaseFeeDao extends CrudRepository<BaseFee, Long>,JpaSpecificationExecutor<BaseFee>{

    public List<BaseFee> findAll();
    public List<BaseFee> findByDelFlag(Integer delFlag);
    public BaseFee findById(long id);
    
    public List<BaseFee> findByDelFlagAndWorkcenterIdAndProcId(Integer delFlag,Long wid,Long procId);
    
    //public List<BaseFee> findByDelFlagAndWorkcenterId(Integer delFlag,Long wid);
    
    public List<BaseFee> findByDelFlagAndWorkcenterIdAndProcIdIsNull(Integer delFlag,Long wid);

    public List<BaseFee> findByDelFlagAndProcId(Integer delFlag,Long procId);


//    @Query(value = " select b from BaseFee b left join BjModelType t on b.mhType = t.modelName where t.delFlag=:delFlag" +
//            " and b.delFlag=:delFlag and b.procId=:procId" +
//            " and t.modelName=:mhType and b.workcenterId =:workcenterId")
    public List<BaseFee> findByDelFlagAndProcIdAndMhTypeAndWorkCenterId(Integer delFlag,Long procId,String mhType,Long workcenterId);


    public List<BaseFee> findByDelFlagAndProcIdAndModelCodeAndWorkCenterId(Integer delFlag,Long procId,String modelCode,Long workcenterId);
    //public int countByDelFlagAndWorkcenterIdAndProcId(Integer delFlag,Long wid,Long procId);//查询此工作中心是否有记录
}