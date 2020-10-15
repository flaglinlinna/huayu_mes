package com.web.produce.service;


import com.app.base.data.ApiResponseResult;

public interface ProductService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;
	
	public ApiResponseResult getHXTaskNo(String keyword) throws Exception;
	
	public ApiResponseResult afterNei(String barcode,String task_no) throws Exception;
	
	public ApiResponseResult afterWai(String nbarcode,String task_no,String wbarcode,String hx,String ptype) throws Exception;
	
	public ApiResponseResult getDetailByTask(String taskNo) throws Exception;
}
