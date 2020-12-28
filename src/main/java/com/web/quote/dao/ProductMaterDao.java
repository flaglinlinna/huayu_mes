package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.ProductMater;


public interface ProductMaterDao extends CrudRepository<ProductMater, Long>,JpaSpecificationExecutor<ProductMater>{

	public List<ProductMater> findAll();
	ProductMater findById(long id);
	public List<ProductMater> findByDelFlag(Integer delFlag);
	
	public List<ProductMater> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsType(Integer delFlag,Long pkQuote,String bsType);
	
	@Query(value = "select map from ProductMater map  where map.delFlag=0 and map.pkQuote=?1 and ( map.bsType='hardware' or  map.bsType='surface' or map.bsType='packag') ")
	 public  List<ProductMater> findByDelFlagAnd3Tyle(Long pkQuote);
	
	@Query(value = "select map from ProductMater map  where map.delFlag=0 and map.pkQuote=?1 and  map.bsType='molding' ")
	 public  List<ProductMater> findByDelFlagAndMolding(Long pkQuote);

}
