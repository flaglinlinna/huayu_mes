package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface AbnormalHoursRService {

	public ApiResponseResult getTaskNo(String keyword)throws Exception;
	
	public ApiResponseResult getLiner(String keyword)throws Exception;
	
	public ApiResponseResult getEmpCode(String keyword)throws Exception;
	
	//public ApiResponseResult getList(String keyword,String sdate,String edate,PageRequest pageRequest) throws Exception;
}
