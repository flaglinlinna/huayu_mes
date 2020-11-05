package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.DevLog;

import java.util.List;


public interface DevLogDao extends CrudRepository<DevLog, Long>,JpaSpecificationExecutor<DevLog>{

	@Modifying
    @Query(value ="update DevLog t set t.cmdFlag=1,t.fmemo=?1 where  t.delFlag=0 and t.cmdFlag=0 and t.devCode=?2 and t.id=?3  ")
    public void updateDelFlagBySn( String fmemo,String sn,Long id);
	
	public List<DevLog> findByDelFlagAndCmdFlagAndDevCodeAndDescription(Integer delFlag,Integer cmd,String sn,String des);
}
