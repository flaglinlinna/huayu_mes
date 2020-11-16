package com.web.basic.dao;

import com.web.basic.entity.ProdErr;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface ProdErrDao extends CrudRepository<ProdErr, Long>,JpaSpecificationExecutor<ProdErr>{

	public List<ProdErr> findAll();
	public List<ProdErr> findByDelFlag(Integer delFlag);
	public ProdErr findById(long id);
	public int countByDelFlagAndErrCode(Integer delFlag, String errCode);//查询Code是否存在
}
