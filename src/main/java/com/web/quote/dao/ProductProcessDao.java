package com.web.quote.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.BjModelType;
import com.web.basePrice.entity.Proc;
import com.web.quote.entity.ProductProcess;


public interface ProductProcessDao extends CrudRepository<ProductProcess, Long>,JpaSpecificationExecutor<ProductProcess>{

	public List<ProductProcess> findAll();
	public ProductProcess findById(long id);
	public List<ProductProcess> findByDelFlag(Integer delFlag);
	
	public List<ProductProcess> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	@Modifying
	@Query(value = "update ProductProcess p set p.bsStatus = 1 ,p.lastupdateBy =?1 ,p.lastupdateDate =?2 where p.pkQuote =?3 and p.bsType =?4 and p.delFlag = 0 ")
	Integer doProcessStatusByType(Long userId, Date date,Long pkQuote,String bsType);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsType(Integer delFlag,Long pkQuote,String bsType);
	
	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsTypeAndBsNameOrderByBsOrderDesc(Integer delFlag,Long pkQuote,String bsType,String bsname);
	
	@Query(value = "select DISTINCT M.BS_NAME from price_product_process M WHERE M.DEL_FLAG=0 AND M.BS_TYPE=?1 AND M.PK_QUOTE=?2 AND M.BS_NAME IS NOT NULL", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String bsType,Long quoteid);
	
	 @Query(value = "select p.model_Code,p.model_Name  from "+BjModelType.TABLE_NAME+" p  where p.del_Flag=0 and p.pk_Workcenter=?1",nativeQuery = true)
	 public  List<Map<String, Object>> findByDelFlagAndWorkcenter(Long pkWorkcenter);

	@Query(value = "select DISTINCT f.model_Code,f.MH_TYPE as MODEL_NAME from BJ_BASE_FEE f  where f.DEL_FLAG = 0 and f.MH_TYPE is not null and f.PROC_ID = ?1 and  f.WORKCENTER_ID = ?2  ORDER BY MODEL_NAME ",nativeQuery = true)
	public  List<Map<String, Object>> findByWorkcenter(Long procId,Long pkWorkcenter);
	 
	 @Query(value = "select p from Proc p  where p.delFlag=0 and p.workcenterId in (select bj.id from BjWorkCenter bj where bj.delFlag=0 and bj.bsCode=?1)")
	 public  List<Proc> getListByType(String code);

	public List<ProductProcess> findByDelFlagAndPkQuoteOrderByBsNameDescBsTypeDescBsOrderAsc(Integer delFlag,Long pkQuote);

	public List<ProductProcess> findByBsNameAndBsElementAndPkQuoteAndBsTypeAndDelFlagOrderByBsOrderDesc(String bsName,String element,Long pkQuote,String bsType,Integer delFlag);
}
