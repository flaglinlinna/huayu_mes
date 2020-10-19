package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.AbnormalHours;

public interface AbnormalHoursService {
	
	public ApiResponseResult add(AbnormalHours abnormalHours)throws Exception;
	
	public ApiResponseResult getList(String keyword, PageRequest pageRequest)throws Exception;
	
	public ApiResponseResult getAbnormalHours(Long id)throws Exception;
	
	public ApiResponseResult edit(AbnormalHours abnormalHours)throws Exception;
	
	public ApiResponseResult delete(Long id)throws Exception;
	
	public ApiResponseResult getEmpInfo(String keyword)throws Exception;
	
	public ApiResponseResult getTaskNo(String keyword)throws Exception;
}
