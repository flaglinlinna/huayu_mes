package com.web.quote.dao;

import com.web.quote.entity.ProductProcessTemp;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductProcessTempDao extends CrudRepository<ProductProcessTemp, Long>,JpaSpecificationExecutor<ProductProcessTemp>{

	public List<ProductProcessTemp> findAll();
	public ProductProcessTemp findById(long id);
	public List<ProductProcessTemp> findAllByIdIn(Long[] ids);
	public List<ProductProcessTemp> findByDelFlag(Integer delFlag);
	public List<ProductProcessTemp> findByDelFlagAndPkQuoteAndBsTypeAndCreateByAndCheckStatus(Integer delFlag,Long pkQuote,String bsType,
																							  Long createBy,Integer checkStatus);

	Integer deleteByPkQuoteAndBsTypeAndCreateBy(Long pkQuote,String bsType,Long createBy);
	public List<ProductProcessTemp> findByDelFlagAndPkQuote(Integer delFlag, String pkQuote);
}
