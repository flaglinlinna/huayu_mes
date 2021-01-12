package com.web.quote.dao;

import java.util.Date;
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
	
	//查询此报价的项目是否已完成
	public int countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(Integer delFlag,Long PkQuote,String bsCode,int bsStatus);
	
	//变更项目进度状态（0未开始、1进行中、2已完成、3不需要填写）
	@Modifying
    @Query("update QuoteItem t set t.bsStatus=?1 where t.pkQuote=?2 and t.bsCode=?3")
    public void switchStatus(int bsStatus,Long quoteId,String bsCode);
	
	//增加开始时间
	@Modifying
    @Query("update QuoteItem t set t.bsBegTime=?1 where t.pkQuote=?2 and t.bsCode=?3")
    public void setBegTime(Date begtime,Long quoteId,String bsCode);
	
	//增加结束时间
	@Modifying
	@Query("update QuoteItem t set t.bsEndTime=?1 where t.pkQuote=?2 and t.bsCode=?3")
	public void setEndTime(Date endTime,Long quoteId,String bsCode);
	
	//增加处理人
	@Modifying
	@Query("update QuoteItem t set t.bsPerson=?1, t.toDoBy=?2 where t.pkQuote=?3 and t.bsCode=?4")
	public void setPerson(String personName,Long personId,Long quoteId,String bsCode);
}
