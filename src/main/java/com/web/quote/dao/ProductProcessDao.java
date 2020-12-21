package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.ProductProcess;


public interface ProductProcessDao extends CrudRepository<ProductProcess, Long>,JpaSpecificationExecutor<ProductProcess>{

	public List<ProductProcess> findAll();
	public ProductProcess findById(long id);
	public List<ProductProcess> findByDelFlag(Integer delFlag);
}
