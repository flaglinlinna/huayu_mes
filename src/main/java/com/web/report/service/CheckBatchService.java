package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface CheckBatchService {

	public ApiResponseResult getDeptInfo(String keyword) throws Exception;
	
	public ApiResponseResult getItemList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getCheckBatchReport(String beginTime,
			String endTime,String deptId,String itemNo, PageRequest pageRequest) throws Exception;
}
