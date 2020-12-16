package com.system.check.service;

import com.app.base.data.ApiResponseResult;
import com.system.check.entity.WorkflowStep;
import org.springframework.data.domain.PageRequest;

/**
 *  流程
 *
 */
public interface WorkflowStepService {

	public ApiResponseResult add(WorkflowStep workflowStep) throws Exception;

	public ApiResponseResult edit(WorkflowStep workflowStep) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(Long bsFlowId, PageRequest pageRequest) throws Exception;

}
