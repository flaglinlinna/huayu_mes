package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.SysParam;

public interface SysParamService {
	public ApiResponseResult add(SysParam sysParam) throws Exception;

	public ApiResponseResult edit(SysParam sysParam) throws Exception;

	// 根据ID获取
	public ApiResponseResult getSysParam(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getValueByCodeList(String keyword) throws Exception;
}
