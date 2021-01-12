package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.QuoteItemBase;


public interface QuoteItemBaseDao extends CrudRepository<QuoteItemBase, Long>,JpaSpecificationExecutor<QuoteItemBase>{

	public List<QuoteItemBase> findByDelFlag(Integer delFlag);

	public  QuoteItemBase findById(long Id);

	public List<QuoteItemBase> findByDelFlagAndBsStyle(Integer delFlag,String bsStyle);
	
	@Query(value = "select map from QuoteItemBase map  where map.delFlag=?1 and bsStyle in ('mater','process') ")
	public  List<QuoteItemBase> findByDelFlagAndStyles(Integer delFlag);

}
