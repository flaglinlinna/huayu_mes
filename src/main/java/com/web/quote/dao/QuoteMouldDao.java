package com.web.quote.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basic.entity.ProdProcDetail;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteMould;


public interface QuoteMouldDao extends CrudRepository<QuoteMould, Long>,JpaSpecificationExecutor<QuoteMould>{

	public List<QuoteMould> findAll();
	public List<QuoteMould> findByDelFlag(Integer delFlag);
	public QuoteMould findById(long id);
	//public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询编号是否存在
	
	public List<QuoteMould> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);
	
	@Modifying
    @Query("update QuoteMould t set t.delFlag=1 where t.bsName=?1 and t.pkQuote=?2 and t.delFlag=0")
    public void delteQuoteMouldByBsNameAndPkQuote(String  bsName,Long pkQuote);//根据组件名称修改表数据
	
	@Query(value = "select distinct t.bs_Element from price_quote_bom t  where t.pk_quote=?1  and t.del_Flag='0' and t.bs_component is not null", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String quoteid);//获取组件列表
	
	@Modifying
    @Query("update QuoteMould t set t.bsStatus=?1 where t.pkQuote=?2 and t.delFlag=0")
    public void saveQuoteMouldByQuoteId(Integer bsStatus,Long  quoteId);//变更字段状态
	
	public int countByDelFlagAndPkQuoteAndBsActQuote(Integer delFlag,Long pkQuote, BigDecimal bsActQuote);//删除同报价单下的同名记录

}
