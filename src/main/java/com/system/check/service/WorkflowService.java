package com.system.check.service;

import com.app.base.data.ApiResponseResult;
import com.system.check.entity.Workflow;
import org.springframework.data.domain.PageRequest;

/**
 *  流程
 *
 */
public interface WorkflowService {

	public ApiResponseResult add(Workflow workflow) throws Exception;

	public ApiResponseResult edit(Workflow workflow) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doStatus(Long id,Integer bsStatus) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

}
