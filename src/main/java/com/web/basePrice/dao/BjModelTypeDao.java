package com.web.basePrice.dao;

import com.web.basePrice.entity.BjModelType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 机台类型维护
 */
public interface BjModelTypeDao extends CrudRepository<BjModelType, Long>,JpaSpecificationExecutor<BjModelType>{

    public List<BjModelType> findAll();
    public List<BjModelType> findByDelFlag(Integer delFlag);
    public BjModelType findById(long id);
    public List<BjModelType> findByDelFlagAndModelCode(Integer delFlag,String modelCode);

//    public List<BjModelType> findByDelFlagAndWorkcenterIdAndProcId(Integer delFlag, Long wid, Long procId);

//    public List<BjModelType> findByDelFlagAndWorkcenterId(Integer delFlag, Long wid);

}