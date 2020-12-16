package com.web.quote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.quote.entity.QuoteItemBase;


public interface QuoteItemBaseDao extends CrudRepository<QuoteItemBase, Long>,JpaSpecificationExecutor<QuoteItemBase>{

	public List<QuoteItemBase> findByDelFlag(Integer delFlag);

}
