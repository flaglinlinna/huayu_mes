package com.web.quote.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.BaseFee;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.Proc;
import com.web.basic.entity.ProdProcDetail;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteProcess;


public interface QuoteProcessDao extends CrudRepository<QuoteProcess, Long>,JpaSpecificationExecutor<QuoteProcess>{

	public List<QuoteProcess> findAll();
	public List<QuoteProcess> findByDelFlag(Integer delFlag);
	public QuoteProcess findById(long id);
	//public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询编号是否存在
	
	public List<QuoteProcess> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(Integer delFlag,Long pkQuote,String name);
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsNameAndBsOrder(Integer delFlag,Long pkQuote,String name,int order);
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsStatus(Integer delFlag,Long pkQuote,int bsStatus);

	List<QuoteProcess> findByDelFlagAndPkQuoteAndPkProcAndBsName(Integer delFlag,Long pkQuote,Long pkProc,String bsName);
	
	@Modifying
    @Query("update QuoteProcess t set t.delFlag=1 where t.pkQuoteBom=?1  and t.pkQuote=?2 and t.delFlag=0")
    public void delteQuoteProcessByPkQuoteBomAndPkQuote(Long pkQuoteBom,Long pkQuote);//根据ID修改表数据
	
	@Modifying
    @Query("update QuoteProcess t set t.delFlag=1 where t.bsName=?1 and t.pkQuote=?2 and t.delFlag=0")
    public void delteQuoteProcessByBsNameAndPkQuote(String  bsName,Long pkQuote);//根据零件名称修改表数据
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteBomAndBsOrder(Integer delFlag,Long pkQuoteBom,Integer bsOrder);
	
	@Query(value = "select distinct t.bs_component from price_quote_bom t  where t.pk_quote=?1  and t.del_Flag='0' and t.bs_component is not null", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String quoteid);

	@Query(value = "select distinct t.PK_BJ_WORK_CENTER, t.BS_ELEMENT,t.BS_COMPONENT,c.WORKCENTER_NAME from price_quote_bom t " +
			" LEFT JOIN BJ_BASE_WORKCENTER c on t.PK_BJ_WORK_CENTER = c.ID where t.pk_quote= ?1  and t.del_Flag= 0 ",
			countQuery =  " select count(1) from (select distinct  t.PK_BJ_WORK_CENTER,t.BS_ELEMENT,t.BS_COMPONENT from price_quote_bom t" +
					" where t.pk_quote= ?1  and t.del_Flag= 0)",
			nativeQuery = true)
	Page<Map<String, Object>> getBomNameByPage(Long quoteId, Pageable pageable);
	
	@Modifying
    @Query("update QuoteProcess t set t.bsStatus=?1 where t.pkQuote=?2 and t.delFlag=0")
    public void saveQuoteProcessByQuoteId(Integer bsStatus,Long  quoteId);//变更字段状态

	@Query(value = "select BS_TYPE as type,count(1) as num from PRICE_PRODUCT_PROCESS where PK_QUOTE = ?1 and DEL_FLAG = 0 GROUP BY  BS_TYPE", nativeQuery = true)
	public List<Map<String, Object>> countByBsType(Long quoteId);
}
