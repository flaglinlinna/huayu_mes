package com.web.produce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.DevLog;

import java.util.List;
import java.util.Map;


public interface DevLogDao extends CrudRepository<DevLog, Long>,JpaSpecificationExecutor<DevLog>{

	@Modifying
    @Query(value ="update DevLog t set t.cmdFlag=1,t.fmemo=?1 where  t.delFlag=0 and t.cmdFlag=0 and t.devCode=?2 and t.id=?3  ")
    public void updateDelFlagBySn( String fmemo,String sn,Long id);
	
	/*@Modifying
    @Query(value ="update DevLog t set t.cmdFlag=1,t.fmemo=?1,  t.cmdFlagFinger1=1 where  t.delFlag=0 and t.cmdFlag=1   and t.devCode=?2 and t.id=?3  ")
    public void updateDelFlagAndF1BySn( String fmemo,String sn,Long id);
	
	@Modifying
    @Query(value ="update DevLog t set t.cmdFlag=1,t.fmemo=?1, t.cmdFlagFinger2=1 where  t.delFlag=0 and t.cmdFlag=1   and t.devCode=?2 and t.id=?3  ")
    public void updateDelFlagAndF2BySn( String fmemo,String sn,Long id);*/
	
	public List<DevLog> findByDelFlagAndCmdFlagAndDevCodeAndDescription(Integer delFlag,Integer cmd,String sn,String des);
	
	public List<DevLog> findByDelFlagAndCmdFlagAndDevCodeAndDescriptionAndCmdFlagFinger1AndCmdFlagFinger2(Integer delFlag,Integer cmd,String sn,String des,Integer f1,Integer f2);
	
}
