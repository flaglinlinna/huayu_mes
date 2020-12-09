package com.web.basePrice.dao;

import com.web.basePrice.entity.CustomQs;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CustomQsDao extends CrudRepository<CustomQs, Long>,JpaSpecificationExecutor<CustomQs>{

    public List<CustomQs> findAll();
    public List<CustomQs> findByDelFlag(Integer delFlag);
    public CustomQs findById(long id);
}