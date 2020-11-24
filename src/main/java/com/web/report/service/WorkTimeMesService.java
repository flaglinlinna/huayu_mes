package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface WorkTimeMesService {
	
	public ApiResponseResult getLinerList(String keyword)throws Exception;
	
	public ApiResponseResult getItemList(String keyword,PageRequest pageRequest)throws Exception;
	
	public ApiResponseResult getList(String sdate,String edate,String liner_id,String itemCode,PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getListDetail(String list_id,PageRequest pageRequest)throws Exception;
}
