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
	 
	 @Query(value = "select map from Quote map  where map.delFlag=0 and map.bsStatus2Hardware=1 and  map.bsStatus2Molding=1 and map.bsStatus2Surface=1 and map.bsStatus2Packag=1 ")
		public  List<Quote> findByDelFlagAndStatus2();
}
