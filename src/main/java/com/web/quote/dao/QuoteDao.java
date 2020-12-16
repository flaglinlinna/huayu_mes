package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.Quote;


public interface QuoteDao extends CrudRepository<Quote, Long>,JpaSpecificationExecutor<Quote>{

	public List<Quote> findAll();
	public List<Quote> findByDelFlag(Integer delFlag);
	public Quote findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询编号是否存在
}
