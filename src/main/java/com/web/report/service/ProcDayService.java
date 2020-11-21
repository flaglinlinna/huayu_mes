package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface ProcDayService {

	public ApiResponseResult getDeptInfo(String keyword) throws Exception;
	
	public ApiResponseResult getItemList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getReport(String beginTime,
			String endTime,String deptId,String itemNo, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getDetailReport(String user_id,String dep_id,String proc_no,String fdate, PageRequest pageRequest) throws Exception;
}
