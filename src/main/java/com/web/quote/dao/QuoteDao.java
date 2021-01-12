package com.web.quote.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.Quote;


public interface QuoteDao extends CrudRepository<Quote, Long>,JpaSpecificationExecutor<Quote>{

	public List<Quote> findAll();
	public List<Quote> findByDelFlag(Integer delFlag);
	public Quote findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询编号是否存在
	
	 @Query(value = "select count(p.id)nums,p.bs_status status from "+Quote.TABLE_NAME+" p where p.del_flag=0 group by p.bs_status ", nativeQuery = true)
	 public List<Map<String, Object>> getNumByStatus();
	 

	 @Query(value = "select count(p.id)nums,p.bs_status4 status from "+Quote.TABLE_NAME+" p where p.del_flag=0 group by p.bs_status4 ", nativeQuery = true)
	 public List<Map<String, Object>> getNumByStatus4();


	@Query(value = "select count(p.id) as nums,bs_status2purchase as status from "+Quote.TABLE_NAME+" p where p.del_flag=0 and p.bs_step =?1 group by bs_status2purchase ", nativeQuery = true)
	public List<Map<String, Object>> getNumByPurchaseAndBsStep(int bsStep);

	@Query(value = "select count(p.id) as nums,bs_status2out as status from "+Quote.TABLE_NAME+" p where p.del_flag=0 and p.bs_step =?1 group by bs_status2out ", nativeQuery = true)
	public List<Map<String, Object>> getNumByOutAndBsStep(int bsStep);

	@Query(value = "select count(p.id) as nums,decode(p.bs_end_time3,null,'1','2') as status from  price_quote p where p.del_flag=0 and p.bs_step >2 group by decode(p.bs_end_time3,null,'1','2')", nativeQuery = true)
	public List<Map<String, Object>> getNumBySumAndBsStep();

	 @Query(value = "select map from Quote map  where map.delFlag=0 and map.bsStatus2Hardware=2 and  map.bsStatus2Molding=2 and map.bsStatus2Surface=2 and map.bsStatus2Packag=2 and map.bsStatus2Out=2 and  map.bsStatus2Purchase=2 and map.id =?1")
	 public  List<Quote> findByDelFlagAndStatus2AndId(Long id);

}
