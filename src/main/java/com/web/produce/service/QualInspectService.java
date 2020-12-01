package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface QualInspectService {

	public ApiResponseResult getProcList(String facoty,String company,String userId,String keyword) throws Exception;//获取检验节点列表

	//扫描箱号条码,带出客户简称、机型、数量、组长，箱号条码。可以扫描多个箱号条码
	public ApiResponseResult scanBarcode(String company,String factory,String user_id,String proc, String barcode) throws Exception;
	
	public ApiResponseResult getDepatrList(String factory,String company,String keyword) throws Exception;
	
	public ApiResponseResult getBadList(String company,String factory,String keyword) throws Exception;
	
	public ApiResponseResult saveData(String factory,String company,String user_id, String proc,String barcodeList,int checkTotal,int badTotal,
			String chkResult,String departCode,String badList)throws Exception;
	
	public ApiResponseResult getHistoryList(String keyword, String hStartTime,String hEndTime,PageRequest pageRequest) throws Exception;
}
