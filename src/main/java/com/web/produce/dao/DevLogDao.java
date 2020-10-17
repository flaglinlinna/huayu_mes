package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.DevLog;

import java.util.List;


public interface DevLogDao extends CrudRepository<DevLog, Long>,JpaSpecificationExecutor<DevLog>{

	public List<DevLog> findAll();
	public List<DevLog> findByDelFlag(Integer delFlag);
	public DevLog findById(long id);	
}
