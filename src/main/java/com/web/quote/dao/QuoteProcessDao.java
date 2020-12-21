package com.web.quote.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basic.entity.ProdProcDetail;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteProcess;


public interface QuoteProcessDao extends CrudRepository<QuoteProcess, Long>,JpaSpecificationExecutor<QuoteProcess>{

	public List<QuoteProcess> findAll();
	public List<QuoteProcess> findByDelFlag(Integer delFlag);
	public QuoteProcess findById(long id);
	//public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询编号是否存在
	
	public List<QuoteProcess> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);
	
	@Modifying
    @Query("update QuoteProcess t set t.delFlag=1 where t.pkQuoteBom=?1 and t.delFlag=0")
    public void delteQuoteProcessByPkQuoteBom(Long pkQuoteBom);//根据ID修改表数据
	
	@Modifying
    @Query("update QuoteProcess t set t.delFlag=1 where t.bsName=?1 and t.delFlag=0")
    public void delteQuoteProcessByBsName(String  bsName);//根据零件名称修改表数据
	
	public List<QuoteProcess> findByDelFlagAndPkQuoteBomAndBsOrder(Integer delFlag,Long pkQuoteBom,Integer bsOrder);
	
	@Query(value = "select distinct t.bs_component from price_quote_bom t  where t.pk_quote=?1  and t.del_Flag='0' and t.bs_component is not null", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String quoteid);
	
	@Modifying
    @Query("update QuoteProcess t set t.bsStatus=1 where t.pkQuote=?1 and t.delFlag=0")
    public void saveQuoteProcessByQuoteId(Long  quoteId);//变更字段状态
}
