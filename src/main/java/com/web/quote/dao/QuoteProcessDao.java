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

	public List<QuoteProcess> findByDelFlagAndPkQuoteAndPkQuoteBom(Integer delFlag,Long pkQuote,Long pkQuoteBom);

	public List<QuoteProcess> findByDelFlagAndPkQuoteOrderById(Integer delFlag,Long pkQuote);

	Integer countByDelFlagAndBsMaterNameAndPkQuote(Integer delFlag,String bsMaterName,Long pkQuote);

	Integer countByDelFlagAndPkQuoteAndPkQuoteBom(Integer delFlag,Long bsMaterName,Long pkQuote);

	@Query(value = "select max(b.bs_order) from PRICE_QUOTE_PROCESS b where b.DEL_FLAG = ?1 and b.PK_QUOTE = ?2  " ,nativeQuery = true)
	Integer findMaxBsOrder(Integer delFlag,Long pkQuote);

	Integer countByDelFlagAndPkQuoteAndPkProcIsNull(Integer delFlag,Long pkQuote);

	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(Integer delFlag,Long pkQuote,String name);

	public List<QuoteProcess> findByDelFlagAndPkQuoteAndBsLinkNameOrderByBsOrder(Integer delFlag,Long pkQuote,String name);
	
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
	@Query(value = "update PRICE_QUOTE_PROCESS set DEL_FLAG =1 where PK_QUOTE_BOM in (select PK_BOM_ID2 from PRICE_QUOTE_BOM where DEL_FLAG = 1 and PK_QUOTE = ?1)",nativeQuery = true)
	public void  deletByQuoteBom(Long pkQuote);

	@Modifying
	@Query("update QuoteProcess t set t.delFlag=1 where t.pkQuote=?1")
	Integer delteQuoteProcessByPkQuote(Long pkQuote);
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteBomAndBsOrder(Integer delFlag,Long pkQuoteBom,Integer bsOrder);
	
	@Query(value = "select distinct t.bs_component from price_quote_bom t  where t.pk_quote=?1  and t.del_Flag='0' and t.bs_component is not null", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String quoteid);

	@Query(value = "SELECT DISTINCT f.PROC_ID id , f.PROC_NAME name FROM BJ_BASE_FEE f where  f.FEE_LH is not NULL and DEL_FLAG = 0 and f.PROC_ID is not null and f.WORKCENTER_ID = ?1", nativeQuery = true)
	public List<Map<String, Object>> getProcByWorkCenter(Long pkWorkCenter);

	@Query(value = "SELECT DISTINCT f.id id , f.PROC_NAME name FROM bj_base_proc f where DEL_FLAG = 0 and f.WORKCENTER_ID = ?1", nativeQuery = true)
	public List<Map<String, Object>> getProcByWorkCenterAndOut(Long pkWorkCenter);

	@Query(value = "select max(t.id) as id ,max(t.PK_BJ_WORK_CENTER) as PK_BJ_WORK_CENTER , max(t.BS_ELEMENT)as BS_ELEMENT ,max(t.BS_COMPONENT) as BS_COMPONENT ,max(t.bs_mater_Name) as bs_mater_Name ,max(c.WORKCENTER_NAME)" +
			" as WORKCENTER_NAME from" +
			" price_quote_bom t " +
			" LEFT JOIN BJ_BASE_WORKCENTER c on t.PK_BJ_WORK_CENTER = c.ID " +
			" left join BJ_BASE_ITEM_TYPE_WG w on w.id = t.PK_ITEM_TYPE_WG where t.pk_quote= ?1  and t.del_Flag= 0 and w.ITEM_TYPE <> '辅料' GROUP BY t.PK_BJ_WORK_CENTER,t.BS_ELEMENT,t.BS_COMPONENT ",
			countQuery =  " select count(*)from(select max(t.id) from price_quote_bom t  left join BJ_BASE_ITEM_TYPE_WG w on w.id = t.PK_ITEM_TYPE_WG where t.pk_quote= ?1 " +
					" and t.del_Flag= 0 and w.ITEM_TYPE <> '辅料' GROUP BY t.PK_BJ_WORK_CENTER,t.BS_ELEMENT,t.BS_COMPONENT)",
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

	@Query(value = "select * from (select pp.id,pp.bs_linK_name,pp.bs_groups,pp.BS_ELEMENT,pp.BS_ORDER,bs_Name," +
			 " bp.PROC_NAME,bw.workcenter_Name from price_quote_process pp left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id" +
			" LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id  where pp.PK_QUOTE = ?1 and pp.BS_GROUPS is null  and pp.DEL_FLAG = 0 " +
			"UNION" +
			" select pp.id,pp.bs_linK_name,pp.bs_groups,pp.BS_ELEMENT,pp.BS_ORDER,bs_Name,bp.PROC_NAME," +
			"bw.workcenter_Name from price_quote_process pp  left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id  where pp.PK_QUOTE = ?1" +
			" and pp.BS_GROUPS is not null and pp.DEL_FLAG = 0 and pp.bs_order in (select max(p.bs_order) from price_quote_process p " +
			" where p.PK_QUOTE = ?1 and p.BS_GROUPS is not null and p.DEL_FLAG = 0 GROUP BY p.BS_ELEMENT,p.bs_link_name,p.BS_GROUPS)  " +
			" ) ORDER BY  BS_ELEMENT,bs_linK_name,BS_ORDER" +
			" ",nativeQuery = true,
			countQuery = "select count(id) from (select pp.id from price_quote_process pp left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id" +
					" LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id  where pp.PK_QUOTE = ?1 and pp.BS_GROUPS is null  and pp.DEL_FLAG = 0" +
					" UNION"  +
					" select max(pp.id) from price_quote_process pp" +
					" left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id where pp.PK_QUOTE = ?1" +
					" and pp.BS_GROUPS is not null  and pp.DEL_FLAG = 0 GROUP BY pp.BS_ELEMENT,pp.bs_link_name,pp.BS_GROUPS)")
	public  Page<Map<String, Object>> getSumList(Long pkQuote,Pageable pageable);

	@Query(value = "select BS_ELEMENT as BSELEMENT,wm_concat(bs_Name) as bsName, max(BS_ORDER) as bsorder,sum(BS_SINGLETON) as BS_SINGLETON from PRICE_QUOTE_PROCESS where PK_QUOTE = ?1 and DEL_FLAG = 0 GROUP BY BS_ELEMENT HAVING sum(BS_SINGLETON) = 0",nativeQuery = true)
	public List<Map<String, Object>> getBsNameGroupByElement(Long quoteId);

	@Modifying
	@Query(value = "DELETE PRICE_QUOTE_PROCESS where PK_QUOTE = ?1 and BS_ELEMENT in (select DISTINCT(BS_ELEMENT)  from PRICE_QUOTE_BOM where BS_SINGLETON = 1 and PK_QUOTE = ?1 ) and bs_name in ?2",nativeQuery = true)
	public Integer deletByBsSingleton(Long pkQuote,List<String> bsName);
}
