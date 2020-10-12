package com.web.produce.service;

import com.app.base.data.ApiResponseResult;

public interface OnlineReworkService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;

	public ApiResponseResult subCode(String taskNo, String type,String barcode,String memo) throws Exception;
	
	public ApiResponseResult getReworkTaskNo(String keyword) throws Exception;//返工历史查询制令单
	
	public ApiResponseResult search(String startTime,String endTime,
			String taskNo,String barcode)throws Exception;
	
}
