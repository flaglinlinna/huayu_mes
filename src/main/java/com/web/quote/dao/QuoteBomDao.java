package com.web.quote.dao;

import com.web.quote.entity.QuoteBom;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface QuoteBomDao extends CrudRepository<QuoteBom, Long>,JpaSpecificationExecutor<QuoteBom>{

	public List<QuoteBom> findAll();
	public List<QuoteBom> findByDelFlag(Integer delFlag);
	public QuoteBom findById(long id);
	
	@Modifying
    @Query("update QuoteBom t set t.bsStatus=1 where t.pkQuote=?1 and t.delFlag=0")
	public void saveQuoteBomByQuoteId(Long quoteId);
	
	public List<QuoteBom> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);
}
