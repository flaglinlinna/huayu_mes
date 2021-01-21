package com.web.quote.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	public List<QuoteItem> findByDelFlagAndPkQuoteAndBsStyleAndBsStatus(Integer delFlag,Long PkQuote,String bsStyle,int bsStatus);
	
	
	public List<QuoteItem> findByDelFlagAndPkQuoteAndBsCode(Integer delFlag,Long PkQuote,String bsCode);
	
	//public List<QuoteItem> findByDelFlagAndPkQuoteAndNotBsEndTime(Integer delFlag,Long PkQuote);
	
	//查询某类型项目的是否完成-20210121
    @Query(value = "select t from QuoteItem t where  t.delFlag=0 and t.pkQuote=?1 and t.bsStyle=?2 and t.bsStatus in (0,1)")
    public List<QuoteItem> getStatusByStype(Long quoteId,String stype);
    
    //查询五金项目的是否完成-20210121
    @Query(value = "select t from QuoteItem t where  t.delFlag=0 and t.pkQuote=?1 and t.bsCode in ('B001','C001') and t.bsStatus in (0,1)")
    public List<QuoteItem> getStatusByHardware(Long quoteId);
    @Query(value = "select t from QuoteItem t where  t.delFlag=0 and t.pkQuote=?1 and t.bsCode in ('B002','C002') and t.bsStatus in (0,1)")
    public List<QuoteItem> getStatusByMolding(Long quoteId);
    @Query(value = "select t from QuoteItem t where  t.delFlag=0 and t.pkQuote=?1 and t.bsCode in ('B003','C003') and t.bsStatus in (0,1)")
    public List<QuoteItem> getStatusBySurface(Long quoteId);
    @Query(value = "select t from QuoteItem t where  t.delFlag=0 and t.pkQuote=?1 and t.bsCode in ('B004','C004') and t.bsStatus in (0,1)")
    public List<QuoteItem> getStatusByPackag(Long quoteId);
    
	//查询此报价的项目是否已完成
	public int countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(Integer delFlag,Long PkQuote,String bsCode,int bsStatus);
	
	//查询项目的状态
    @Query(value = "select t.bs_status from price_quote_item t where t.pk_quote=?1 and t.bs_code=?2",nativeQuery = true)
    public List<Map<String, Object>> getItemBsStatus(Long quoteId,String bsCode);
	
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
