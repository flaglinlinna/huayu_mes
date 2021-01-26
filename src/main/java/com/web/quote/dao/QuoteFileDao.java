package com.web.quote.dao;

import com.web.quote.entity.QuoteFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface QuoteFileDao extends CrudRepository<QuoteFile, Long>,JpaSpecificationExecutor<QuoteFile>{

	public List<QuoteFile> findAll();
	public List<QuoteFile> findByDelFlag(Integer delFlag);
	public List<QuoteFile> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);
	public QuoteFile findById(long id);
	
	@Modifying
    @Query("update QuoteFile t set t.bsStatus=1 where t.pkQuote=?1 and t.delFlag=0")
    public void saveQuoteFileByQuoteId(Long  quoteId);//变更字段状态
}
