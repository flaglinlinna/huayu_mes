package com.web.po.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.po.entity.DatabaseInfo;

public interface DatabaseInfoDao extends  CrudRepository<DatabaseInfo, Long>, JpaSpecificationExecutor<DatabaseInfo>  {
	
	List<DatabaseInfo> findAll();
}
