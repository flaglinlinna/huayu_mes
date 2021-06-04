package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.MjProcFee;

import java.util.List;

/**
 * @author lst
 * @date Dec 8, 2020 11:24:36 AM
 */
public interface MjProcFeeDao extends CrudRepository<MjProcFee, Long>,JpaSpecificationExecutor<MjProcFee>{

    public List<MjProcFee> findAll();
    public List<MjProcFee> findByDelFlag(Integer delFlag);
    public MjProcFee findById(long id);
    
    int countByDelFlagAndProductCode(Integer delFlag,String productCode);

    public List<MjProcFee> findByDelFlagAndProductCode(Integer delFlag,String productCode);
}