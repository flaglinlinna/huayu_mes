package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface MesHrDifferService {
	
	public ApiResponseResult getEmpCode(String keyword)throws Exception;
	
	public ApiResponseResult getList(String sdate,String edate,String empCode,PageRequest pageRequest) throws Exception;
}
