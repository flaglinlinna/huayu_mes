package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.MjProcPrice;

import java.util.List;

/**
 * @author lst
 * @date Dec 8, 2020 5:39:36 PM
 */
public interface MjProcPriceDao extends CrudRepository<MjProcPrice, Long>,JpaSpecificationExecutor<MjProcPrice>{

    public List<MjProcPrice> findAll();
    public List<MjProcPrice> findByDelFlag(Integer delFlag);
    public MjProcPrice findById(long id);
}