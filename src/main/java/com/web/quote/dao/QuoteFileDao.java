package com.web.quote.dao;

import com.web.quote.entity.QuoteFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface QuoteFileDao extends CrudRepository<QuoteFile, Long>,JpaSpecificationExecutor<QuoteFile>{

	public List<QuoteFile> findAll();
	public List<QuoteFile> findByDelFlag(Integer delFlag);
	public QuoteFile findById(long id);
}
