package com.web.basePrice.dao;

import com.web.basePrice.entity.BjModelType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
    public List<BjModelType> findByDelFlagAndModelName(Integer delFlag,String modelName);

    @Query(value = "select count(1) from BJ_MODEL_TYPE b where b.DEL_FLAG = 0 and (b.MODEL_NAME = ?1 or b.MODEL_CODE = ?2)",nativeQuery = true)
    Integer countByModelCodeOrModelName(String modelName,String modelCode);
//    public List<BjModelType> findByDelFlagAndWorkcenterIdAndProcId(Integer delFlag, Long wid, Long procId);

//    public List<BjModelType> findByDelFlagAndWorkcenterId(Integer delFlag, Long wid);

}