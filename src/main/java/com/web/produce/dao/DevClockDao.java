package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.DevClock;

import java.util.List;


public interface DevClockDao extends CrudRepository<DevClock, Long>,JpaSpecificationExecutor<DevClock>{

	public List<DevClock> findAll();
	public List<DevClock> findByDelFlag(Integer delFlag);
	public DevClock findById(long id);
	public int countByDelFlagAndDevCode(Integer delFlag, String devCode);//查询devCode是否存在
}
