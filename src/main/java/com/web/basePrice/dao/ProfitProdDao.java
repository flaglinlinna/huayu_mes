package com.web.basePrice.dao;

import com.web.basePrice.entity.ProfitProd;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProfitProdDao extends CrudRepository<ProfitProd, Long>,JpaSpecificationExecutor<ProfitProd>{

    public List<ProfitProd> findAll();
    public List<ProfitProd> findByDelFlag(Integer delFlag);
    public ProfitProd findById(long id);
}