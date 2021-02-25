package com.web.quote.dao;

import com.web.quote.entity.QuoteBom;
import com.web.quote.entity.QuoteBomTemp;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface QuoteBomTempDao extends CrudRepository<QuoteBomTemp, Long>,JpaSpecificationExecutor<QuoteBomTemp>{

	public List<QuoteBomTemp> findAll();
	public List<QuoteBomTemp> findByDelFlag(Integer delFlag);
	public QuoteBomTemp findById(long id);

	Integer deleteByPkQuoteAndCreateBy(Long pkQuote,Long createBy);

	public List<QuoteBomTemp> findByCheckStatusAndDelFlagAndCreateByAndPkQuote(Integer checkStatus,Integer delFlag,Long createBy,Long pkQuote);
	public List<QuoteBom> findByDelFlagAndPkQuote(Integer delFlag, Long pkQuote);

	Integer deleteByIdIn(long[] ids);
}
