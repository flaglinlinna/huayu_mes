package com.web.basic.service;



import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.BarcodeRule;

public interface BarcodeRuleService {
	public ApiResponseResult add(BarcodeRule barcodeRule) throws Exception;

	public ApiResponseResult edit(BarcodeRule barcodeRule) throws Exception;

	public ApiResponseResult getMtrial(String keyword,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getCustomer(String keyword,PageRequest pageRequest) throws Exception;

	//年、月、日、流水号下拉框
	public ApiResponseResult getBarList(String type,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getFsampleByForm(String fixValue, String fyear , String fmonth,String fday,String serialNum,
											  String serialLen) throws Exception;

	public ApiResponseResult addByProc(BarcodeRule barcodeRule) throws Exception;

	// 根据ID获取
	public ApiResponseResult getBarcodeRule(Long id) throws Exception;

	public ApiResponseResult delete(String ids) throws Exception;

	public ApiResponseResult getListByPrc(String keyword, PageRequest pageRequest) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getMtrialList() throws Exception;

	public ApiResponseResult getMtrialList(String keyword, PageRequest pageRequest) throws Exception;
}
