package com.web.produce.service;


import com.app.base.data.ApiResponseResult;

public interface InputService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;
	
	public ApiResponseResult getInfoBarcode(String barcode) throws Exception;
	
	public ApiResponseResult addPut(String barcode,String task_no,String item_no,String qty) throws Exception;
	
	public ApiResponseResult delete(String id) throws Exception;
}