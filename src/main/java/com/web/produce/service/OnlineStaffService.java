package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface OnlineStaffService {
	
	//public ApiResponseResult edit(String params)throws Exception;//修改主表
	
	//public ApiResponseResult deleteVice(Long id)throws Exception;//删除从表数据
	
	//public ApiResponseResult deleteMain(Long id)throws Exception;//删除主表数据
	
	public ApiResponseResult getList(String keyword, PageRequest pageRequest)throws Exception;
	
	public ApiResponseResult getMain(Long id)throws Exception;//获取主表单条记录
	
	public ApiResponseResult getMainInfo(Long id)throws Exception;//获取主表单条记录
	
}
