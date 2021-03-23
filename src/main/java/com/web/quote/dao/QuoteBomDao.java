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
	
	@Modifying
    @Query("update QuoteBom t set t.bsStatus=?2 where t.pkQuote=?1 and t.delFlag=0")
	public void saveQuoteBomByQuoteId(Long quoteId,Integer bsStatus);
	
	public List<QuoteBom> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	@Query(value = " select c.BS_CODE as bsCode, count(m.PRODUCT_RETRIAL) as RETRIAL from PRICE_QUOTE_BOM  m LEFT JOIN " +
			" BJ_BASE_WORKCENTER c on c.id = m.PK_BJ_WORK_CENTER  where m.PK_QUOTE = ?1 GROUP BY c.BS_CODE",nativeQuery = true)
	public List<Map<String, Object>> getRetrial(Long quoteId);

	public List<QuoteBom> findByDelFlagAndOutRetrial(Integer delFlag,Integer retrial);

	public List<QuoteBom> findByDelFlagAndPurchaseRetrial(Integer delFlag,Integer retrial);
}
