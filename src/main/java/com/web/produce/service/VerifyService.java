package com.web.produce.service;


import com.app.base.data.ApiResponseResult;

public interface VerifyService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;
	
	public ApiResponseResult getInfoAdd() throws Exception;
	
	public ApiResponseResult getUserByLine(String lineId) throws Exception;
	
	public ApiResponseResult save(String task_no,String line_id,
			String hour_type,String class_id,String wdate,String emp_ids) throws Exception;

}
