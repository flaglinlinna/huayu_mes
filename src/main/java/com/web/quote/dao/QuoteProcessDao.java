package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteProcess;


public interface QuoteProcessDao extends CrudRepository<QuoteProcess, Long>,JpaSpecificationExecutor<QuoteProcess>{

	public List<QuoteProcess> findAll();
	public List<QuoteProcess> findByDelFlag(Integer delFlag);
	public QuoteProcess findById(long id);
	//public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询编号是否存在
}
