package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface CheckReturnService {
	
	public ApiResponseResult getDeptInfo(String keyword) throws Exception;

	public ApiResponseResult getList(String month,String deptId,PageRequest pageRequest) throws Exception;
}
