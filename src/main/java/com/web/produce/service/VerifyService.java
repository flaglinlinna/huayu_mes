package com.web.produce.service;


import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface VerifyService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;
	
	public ApiResponseResult getInfoAdd() throws Exception;
	
	public ApiResponseResult getUserByLine(String lineId,PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult save(String task_no,String line_id,
			String hour_type,String class_id,String wdate,String emp_ids) throws Exception;
	
	public ApiResponseResult getInfoCreateReturn() throws Exception;

	public ApiResponseResult add(String task_no,String item_no,String liner_name,String qty,String pdate) throws Exception;
	
	public ApiResponseResult getHistoryList(String keyword, String hStartTime,String hEndTime,PageRequest pageRequest) throws Exception;
	
}
