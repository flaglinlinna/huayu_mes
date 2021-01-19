package com.web.quote.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.BjModelType;
import com.web.quote.entity.ProductProcess;


public interface ProductProcessDao extends CrudRepository<ProductProcess, Long>,JpaSpecificationExecutor<ProductProcess>{

	public List<ProductProcess> findAll();
	public ProductProcess findById(long id);
	public List<ProductProcess> findByDelFlag(Integer delFlag);
	
	public List<ProductProcess> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsType(Integer delFlag,Long pkQuote,String bsType);
	
	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsTypeAndBsNameOrderByBsOrderDesc(Integer delFlag,Long pkQuote,String bsType,String bsname);
	
	@Query(value = "select DISTINCT M.BS_NAME from price_product_process M WHERE M.DEL_FLAG=0 AND M.BS_TYPE=?1 AND M.PK_QUOTE=?2 AND M.BS_NAME IS NOT NULL", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String bsType,Long quoteid);
	
	 @Query(value = "select p.model_Code,p.model_Name  from "+BjModelType.TABLE_NAME+" p  where p.del_Flag=0 and p.pk_Workcenter=?1",nativeQuery = true)
	 public  List<Map<String, Object>> findByDelFlagAndWorkcenter(Long pkWorkcenter);
}
