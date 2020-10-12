package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.Issue;

import java.util.List;


public interface IssueDao extends CrudRepository<Issue, Long>,JpaSpecificationExecutor<Issue>{

	public List<Issue> findAll();
	public List<Issue> findByDelFlag(Integer delFlag);
	public Issue findById(long id);
	public  List<Issue> findByDelFlagAndDevClockId(Integer delFlag,long devClockId);
	public int countByDelFlagAndEmpIdAndDevClockId(Integer delFlag, Long empId,Long devClockId);
	
	
}
