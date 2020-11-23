package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface WorkTimeService {
	
	public ApiResponseResult getEmpCode(String keyword)throws Exception;
	
	public ApiResponseResult getList(String sdate,String edate,String line_id,String empCode,PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getListDetail(String list_id)throws Exception;
}
