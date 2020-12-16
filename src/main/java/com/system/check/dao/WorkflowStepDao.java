package com.system.check.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import com.system.check.entity.WorkflowStep;


public interface WorkflowStepDao extends CrudRepository<WorkflowStep, Long>, JpaSpecificationExecutor<WorkflowStep> {
//	Integer countByDelFlagA(Integer delFlag,String bsFlowCode);
//
	WorkflowStep findById(long id);
	
}
