package com.web.produce.service;

import com.app.base.data.ApiResponseResult;

public interface QualInspectService {

	public ApiResponseResult getProcList(String keyword) throws Exception;

	public ApiResponseResult scanBarcode(String proc, String barcode) throws Exception;
	
	public ApiResponseResult getDepatrList(String keyword) throws Exception;
	
	public ApiResponseResult getBadList(String keyword) throws Exception;
	
	public ApiResponseResult saveData(String proc,String barcodeList,Integer checkTot,Integer badTot,
			String result,String departCode,String badList)throws Exception;
	
}
