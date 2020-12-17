package com.web.quote.dao;

import com.web.quote.entity.QuoteBom;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface QuoteBomDao extends CrudRepository<QuoteBom, Long>,JpaSpecificationExecutor<QuoteBom>{

	public List<QuoteBom> findAll();
	public List<QuoteBom> findByDelFlag(Integer delFlag);
	public QuoteBom findById(long id);
}
