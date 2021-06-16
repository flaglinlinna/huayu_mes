package com.web.quote.dao;

import com.web.quote.entity.QuoteBom;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;


public interface QuoteBomDao extends CrudRepository<QuoteBom, Long>,JpaSpecificationExecutor<QuoteBom>{

	public List<QuoteBom> findAll();
	public List<QuoteBom> findByDelFlag(Integer delFlag);
	public QuoteBom findById(long id);



	public QuoteBom findByPkBomId2AndPkQuote(Long PkBomId2,Long pkQuote);
	
	@Modifying
    @Query("update QuoteBom t set t.bsStatus=?2 where t.pkQuote=?1 and t.delFlag=0")
	public Integer saveQuoteBomByQuoteId(Long quoteId,Integer bsStatus);

	@Modifying
	@Query("update QuoteBom t set t.delFlag = 1 where t.pkQuote=?1")
	Integer deleteAllByPkQuote(Long quoteId);
	
	public List<QuoteBom> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	public List<QuoteBom> findByDelFlagAndPkQuoteAndBsComponent(Integer delFlag,Long pkQuote,String bsComponent);

	public List<QuoteBom> findByDelFlagAndPkQuoteAndPkBomIdIsNull(Integer delFlag,Long pkQuote);

	public List<QuoteBom> findByDelFlagAndPkQuoteOrderById(Integer delFlag,Long pkQuote);

	public List<QuoteBom> findByDelFlagAndPkQuoteAndBsMaterNameIsNotNullOrderById(Integer delFlag,Long pkQuote);

	@Query(value = "select distinct t.id id,t.bs_mater_name as bsMaterName,t.bs_groups as bsgroups from price_quote_bom t  where t.pk_quote=?1  and t.del_Flag=0 and t.bs_element = ?2 and t.bs_component =?3 and t.pk_bj_work_center = ?4", nativeQuery = true)
	public List<Map<String, Object>> getBsMaterName(Long quoteId,String bsElement,String component,Long pkWorkCenterId);

	@Query(value = "select distinct t.bs_groups as bsGroups from price_quote_bom t  where t.pk_quote=?1  and t.del_Flag=0 and t.bs_element = ?2 and t.bs_component =?3 and t.pk_bj_work_center = ?4 and t.bs_groups is not null ", nativeQuery = true)
	public List<Map<String, Object>> getBsGroups(Long quoteId,String bsElement,String component,Long pkWorkCenterId);

	@Query(value = "select b.BS_COMPONENT as bsComponent from PRICE_QUOTE_BOM b where b.id in( select max(b.id) FROM PRICE_QUOTE_BOM b left JOIN BJ_BASE_ITEM_TYPE_WG t on b.PK_ITEM_TYPE_WG = t.id where b.PK_QUOTE = ?1 and t.ITEM_TYPE !='辅料' and b.del_flag = 0 GROUP BY  b.BS_COMPONENT ) and  b.PK_QUOTE = ?1 and b.del_flag = 0  and b.BS_ELEMENT = ?2 order by b.id ", nativeQuery = true)
	List<Map<String, Object>> getBsComponent(Long quoteId,String bsElement);

	public List<QuoteBom> findByDelFlagAndBsMaterName(Integer delFlag,String bsMaterName);

	@Query(value = " select c.BS_CODE as bsCode, sum(m.PRODUCT_RETRIAL) as RETRIAL from PRICE_QUOTE_BOM  m LEFT JOIN " +
			" BJ_BASE_WORKCENTER c on c.id = m.PK_BJ_WORK_CENTER  where m.PK_QUOTE = ?1 GROUP BY c.BS_CODE",nativeQuery = true)
	public List<Map<String, Object>> getRetrial(Long quoteId);

	public List<QuoteBom> findByDelFlagAndOutRetrial(Integer delFlag,Integer retrial);

	public List<QuoteBom> findByDelFlagAndPurchaseRetrial(Integer delFlag,Integer retrial);
}
