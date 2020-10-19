package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.PatchCard;

public interface PatchCardService {
	
	public ApiResponseResult add(PatchCard patchCard)throws Exception;
	
	public ApiResponseResult getList(String keyword, PageRequest pageRequest)throws Exception;
	
	public ApiResponseResult getPatchCard(Long id)throws Exception;
	
	public ApiResponseResult edit(PatchCard patchCard)throws Exception;
	
	public ApiResponseResult delete(Long id)throws Exception;
	
	public ApiResponseResult getEmpInfo(String keyword)throws Exception;
	
	public ApiResponseResult getTaskNo(String keyword)throws Exception;
}
