package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.AbnormalProduct;

public interface AbnormalProductService {
	
	public ApiResponseResult add(AbnormalProduct abnormalProduct)throws Exception;
	
	public ApiResponseResult getList(String keyword, PageRequest pageRequest)throws Exception;
	
	public ApiResponseResult getAbnormalHours(Long id)throws Exception;
	
	public ApiResponseResult edit(AbnormalProduct abnormalProduct)throws Exception;
	
	public ApiResponseResult delete(String id)throws Exception;
	
	public ApiResponseResult getErrorInfo()throws Exception;
	
	public ApiResponseResult getTaskNo(String keyword)throws Exception;
	
	public ApiResponseResult getTaskNoInfo(String taskNo)throws Exception;
}
