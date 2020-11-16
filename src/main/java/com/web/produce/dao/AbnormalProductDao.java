package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.AbnormalProduct;


public interface AbnormalProductDao extends CrudRepository<AbnormalProduct, Long>,JpaSpecificationExecutor<AbnormalProduct>{
	
	public AbnormalProduct findById(long id);
}
