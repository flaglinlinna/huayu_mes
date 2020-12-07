package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.PriceComm;

import java.util.List;

/**
 * @author lst
 * @date Dec 7, 2020 5:02:33 PM
 */
public interface PriceCommDao extends CrudRepository<PriceComm, Long>,JpaSpecificationExecutor<PriceComm>{

    public List<PriceComm> findAll();
    public List<PriceComm> findByDelFlag(Integer delFlag);
    public PriceComm findById(long id);
}