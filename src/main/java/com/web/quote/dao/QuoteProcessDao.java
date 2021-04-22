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

	Integer countByDelFlagAndBsMaterNameAndPkQuote(Integer delFlag,String bsMaterName,Long pkQuote);

	Integer countByDelFlagAndPkQuoteAndPkQuoteBom(Integer delFlag,Long bsMaterName,Long pkQuote);

	@Query(value = "select max(b.bs_order) from PRICE_QUOTE_PROCESS b where b.DEL_FLAG = ?1 and b.PK_QUOTE = ?2  " ,nativeQuery = true)
	Integer findMaxBsOrder(Integer delFlag,Long pkQuote);

	Integer countByDelFlagAndPkQuoteAndPkProcIsNull(Integer delFlag,Long pkQuote);

	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(Integer delFlag,Long pkQuote,String name);
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsNameAndBsOrder(Integer delFlag,Long pkQuote,String name,int order);

	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsOrderAndIdIsNot(Integer delFlag,Long pkQuote,int order,Long id);
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsStatusOrderByBsOrder(Integer delFlag,Long pkQuote,int bsStatus);

	List<QuoteProcess> findByDelFlagAndPkQuoteAndPkProcAndBsName(Integer delFlag,Long pkQuote,Long pkProc,String bsName);
	
	@Modifying
    @Query("update QuoteProcess t set t.delFlag=1 where t.pkQuoteBom=?1  and t.pkQuote=?2 and t.delFlag=0")
    public void delteQuoteProcessByPkQuoteBomAndPkQuote(Long pkQuoteBom,Long pkQuote);//根据ID修改表数据
	
	@Modifying
    @Query("update QuoteProcess t set t.delFlag=1 where t.bsName=?1 and t.pkQuote=?2 and t.delFlag=0")
    public void delteQuoteProcessByBsNameAndPkQuote(String  bsName,Long pkQuote);//根据零件名称修改表数据

	@Modifying
	@Query("update QuoteProcess t set t.delFlag=1 where t.pkQuote=?1")
	Integer delteQuoteProcessByPkQuote(Long pkQuote);
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteBomAndBsOrder(Integer delFlag,Long pkQuoteBom,Integer bsOrder);
	
	@Query(value = "select distinct t.bs_component from price_quote_bom t  where t.pk_quote=?1  and t.del_Flag='0' and t.bs_component is not null", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String quoteid);

	@Query(value = "SELECT DISTINCT f.PROC_ID id , f.PROC_NAME name FROM BJ_BASE_FEE f where  f.FEE_LH is not NULL and DEL_FLAG = 0 and f.PROC_ID is not null and f.WORKCENTER_ID = ?1", nativeQuery = true)
	public List<Map<String, Object>> getProcByWorkCenter(Long pkWorkCenter);

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

	@Query(value = "SELECT p.PK_QUOTE_BOM,COUNT(p.PK_QUOTE_BOM)  FROM PRICE_QUOTE_PROCESS p where p.PK_QUOTE =?1 and p.DEL_FLAG = 0 GROUP BY p.PK_QUOTE_BOM HAVING  COUNT( p.PK_QUOTE_BOM ) >1",nativeQuery = true)
	public List<Map<String, Object>> getPkQuoteBomNum(Long quoteId);

	@Query(value = "SELECT p.bs_Groups,COUNT(p.bs_Groups)  FROM PRICE_QUOTE_PROCESS p where p.PK_QUOTE =?1 and p.DEL_FLAG = 0 GROUP BY p.bs_Groups HAVING  COUNT( p.bs_Groups ) >1",nativeQuery = true)
	public List<Map<String, Object>> getBsGroupsNum(Long quoteId);
}
