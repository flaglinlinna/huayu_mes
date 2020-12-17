package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.QuoteItem;


public interface QuoteItemDao extends CrudRepository<QuoteItem, Long>,JpaSpecificationExecutor<QuoteItem>{

	public List<QuoteItem> findAll();
	public List<QuoteItem> findByDelFlag(Integer delFlag);
	public QuoteItem findById(long id);
	public List<QuoteItem> findByDelFlagAndPkQuote(Integer delFlag,Long PkQuote);
}
