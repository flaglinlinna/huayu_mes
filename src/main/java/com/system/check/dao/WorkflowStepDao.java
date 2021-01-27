package com.system.check.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.check.entity.Workflow;
import com.system.check.entity.WorkflowStep;


public interface WorkflowStepDao extends CrudRepository<WorkflowStep, Long>, JpaSpecificationExecutor<WorkflowStep> {
//	Integer countByDelFlagA(Integer delFlag,String bsFlowCode);
//
	WorkflowStep findById(long id);

	Integer countByBsFlowIdAndDelFlag(Long bsFlowId,Integer delFlag);

	Integer countByBsFlowIdAndDelFlagAndBsCheckGrade(Long bsFlowId,Integer delFlag,Integer bsCheckGrade);
	
	@Query(value = "select p.*  from "+ WorkflowStep.TABLE_NAME +" p  left join "+Workflow.TABLE_NAME+" w on w.id = p.bs_Flow_Id "+
			" where p.bs_Check_Grade = (?1)  and p.del_flag=0 and w.bs_Flow_Code=(?2) ", nativeQuery = true)
     public List<WorkflowStep> findAllByCheckCode(int step,String checkCode);
	
	
	@Query(value = "select p.*  from "+ WorkflowStep.TABLE_NAME +" p  left join "+Workflow.TABLE_NAME+" w on w.id = p.bs_Flow_Id "+
			" where  p.del_flag=0 and w.bs_Flow_Code=(?1) order by p.bs_Check_Grade ", nativeQuery = true)
     public List<WorkflowStep> findByCheckCode(String checkCode);
	
}
