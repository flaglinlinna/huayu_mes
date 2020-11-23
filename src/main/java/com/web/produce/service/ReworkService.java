package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import org.springframework.data.domain.PageRequest;

public interface ReworkService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;

	public ApiResponseResult subCode(String taskNo, String type,String barcode,String memo) throws Exception;
	
	public ApiResponseResult getReworkTaskNo(String keyword) throws Exception;//返工历史查询制令单
	
	public ApiResponseResult search(String startTime, String endTime,
									String taskNo, String barcode, PageRequest pageRequest)throws Exception;
	
}
