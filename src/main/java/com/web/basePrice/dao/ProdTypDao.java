package com.web.basePrice.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.ProdTyp;

import java.util.List;

/**
 * @author lst
 * @date Dec 4, 2020 5:24:36 PM
 */
public interface ProdTypDao extends CrudRepository<ProdTyp, Long>,JpaSpecificationExecutor<ProdTyp>{

    public List<ProdTyp> findAll();
    public List<ProdTyp> findByDelFlag(Integer delFlag);
    int countByDelFlagAndProductType(Integer delFlag,String productType);
    public ProdTyp findById(long id);
}