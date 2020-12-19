package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.QuoteItem;


public interface QuoteItemDao extends CrudRepository<QuoteItem, Long>,JpaSpecificationExecutor<QuoteItem>{

	public List<QuoteItem> findAll();
	public List<QuoteItem> findByDelFlag(Integer delFlag);
	public QuoteItem findById(long id);
	public List<QuoteItem> findByDelFlagAndPkQuote(Integer delFlag,Long PkQuote);
	public List<QuoteItem> findByDelFlagAndPkQuoteAndBsStyle(Integer delFlag,Long PkQuote,String bsStyle);
	
	public List<QuoteItem> findByDelFlagAndPkQuoteAndBsCode(Integer delFlag,Long PkQuote,String bsCode);
	
	//public List<QuoteItem> findByDelFlagAndPkQuoteAndNotBsEndTime(Integer delFlag,Long PkQuote);
}
