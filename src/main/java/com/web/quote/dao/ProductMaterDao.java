package com.web.quote.dao;

import com.web.quote.entity.ProductMater;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductMaterDao extends CrudRepository<ProductMater, Long>,JpaSpecificationExecutor<ProductMater>{

	public List<ProductMater> findAll();
	ProductMater findById(long id);
	public List<ProductMater> findByDelFlag(Integer delFlag);
	
	public List<ProductMater> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsType(Integer delFlag,Long pkQuote,String bsType);
}
