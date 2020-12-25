package com.web.quote.dao;


import com.web.quote.entity.ProductMaterTemp;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductMaterTempDao extends CrudRepository<ProductMaterTemp, Long>,JpaSpecificationExecutor<ProductMaterTemp>{

	public List<ProductMaterTemp> findAll();
	ProductMaterTemp findById(long id);
	public List<ProductMaterTemp> findByDelFlag(Integer delFlag);
	public List<ProductMaterTemp> findByDelFlagAndPkQuote(Integer delFlag, Long pkQuote);
	public List<ProductMaterTemp> findByDelFlagAndPkQuoteAndCreateBy(Integer delFlag,Long pkQuote,Long createBy);

	Integer deleteByPkQuoteAndBsTypeAndCreateBy(Long pkQuote,String bsType ,Long createBy);

	Integer deleteByPkQuoteAndCreateByAndBsPurchase(Long pkQuote,Long createBy,Integer purchases);

	//采购填报价格 确定导入数据
	public List<ProductMaterTemp> findByDelFlagAndPkQuoteAndCreateByAndBsPurchaseAndCheckStatus(Integer delFlag,Long pkQuote,Long userId,Integer bsPurchase, Integer status);

	//制造部材料 确定导入数据
	public List<ProductMaterTemp> findByDelFlagAndPkQuoteAndCreateByAndBsTypeAndCheckStatus(Integer delFlag,Long pkQuote,Long userId,String bsType, Integer status);
}
