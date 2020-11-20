package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface LineDayService {

	public ApiResponseResult getDeptInfo(String keyword) throws Exception;
	
	public ApiResponseResult getItemList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getList(String beginTime,
			String endTime,String deptId,String itemNo,String devc_id, PageRequest pageRequest) throws Exception;
}
