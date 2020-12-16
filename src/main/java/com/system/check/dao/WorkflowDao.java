package com.system.check.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.system.check.entity.Workflow;


public interface WorkflowDao extends CrudRepository<Workflow, Long>, JpaSpecificationExecutor<Workflow> {

//	public List<Workflow> findByIsDelAndBsFlowCode(Integer bsIsDel, String bsFlowCode);

	Integer countByDelFlagAndBsFlowCode(Integer delFlag,String bsFlowCode);

	Workflow findById(long id);

//	public List<Workflow> findByIsDelAndBsFlowName(Integer bsIsDel, String bsFlowName);
	
}
