package com.web.report.service;

import com.app.base.data.ApiResponseResult;

public interface CheckBatchService {

	public ApiResponseResult getDeptInfo(String keyword) throws Exception;
	
	public ApiResponseResult getItemList(String keyword) throws Exception;
	
	public ApiResponseResult getCheckBatchReport(String beginTime,
			String endTime,String deptId,String itemNo) throws Exception;
}
