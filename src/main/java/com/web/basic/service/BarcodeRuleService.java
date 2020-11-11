package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.BarcodeRule;

public interface BarcodeRuleService {
	public ApiResponseResult add(BarcodeRule barcodeRule) throws Exception;

	public ApiResponseResult edit(BarcodeRule barcodeRule) throws Exception;

	// 根据ID获取
	public ApiResponseResult getBarcodeRule(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getMtrialList() throws Exception;

	public ApiResponseResult getMtrialList(String keyword, PageRequest pageRequest) throws Exception;
}
