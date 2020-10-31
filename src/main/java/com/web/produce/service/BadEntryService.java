package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface BadEntryService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;//制令单号
	
	public ApiResponseResult getBadInfo(String keyword)throws Exception;//获取不良信息

	public ApiResponseResult checkBarCode(String taskNo,String barCode) throws Exception;//扫描条码（检查条码合法性并带出数量）
	
	public ApiResponseResult saveBad(String taskNo,String barCode,
			int qty,String defCode,String memo) throws Exception;//保存不良
	
	public ApiResponseResult deleteBad(String recordId) throws  Exception;//删除不良
	
	public ApiResponseResult getDetailByTask(String taskNo) throws Exception;
	
	
	public ApiResponseResult getHistoryList(String keyword, String hStartTime,String hEndTime,PageRequest pageRequest) throws Exception;
}
