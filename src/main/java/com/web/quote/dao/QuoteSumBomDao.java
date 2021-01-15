package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.QuoteSumBom;


public interface QuoteSumBomDao extends CrudRepository<QuoteSumBom, Long>,JpaSpecificationExecutor<QuoteSumBom>{

	public List<QuoteSumBom> findAll();
	public List<QuoteSumBom> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);
	public QuoteSumBom findById(long id);


}
