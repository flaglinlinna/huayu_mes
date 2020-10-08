package com.web.produce.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.Clear;


public interface ClearService {
	public ApiResponseResult add(String devList,String empList) throws Exception;

	//public ApiResponseResult edit(Clear issue) throws Exception;

	// 根据ID获取
	public ApiResponseResult getClear(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getEmp(String empKeyword, PageRequest pageRequest) throws Exception;
	public ApiResponseResult getDev(String devKeyword, PageRequest pageRequest) throws Exception;
}
