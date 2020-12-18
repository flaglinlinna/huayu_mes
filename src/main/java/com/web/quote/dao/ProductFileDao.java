package com.web.quote.dao;

import com.web.quote.entity.ProductFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductFileDao extends CrudRepository<ProductFile, Long>,JpaSpecificationExecutor<ProductFile>{

	public List<ProductFile> findAll();
	public List<ProductFile> findByDelFlag(Integer delFlag);
	public ProductFile findById(long id);
}
