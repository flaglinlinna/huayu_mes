package com.web.kanban.service;

import com.app.base.data.ApiResponseResult;

import java.util.List;

import org.springframework.data.domain.PageRequest;

public interface KanbanService {

	public ApiResponseResult getCjbgList(String class_no,String dep_id,String sdata,String edata,String dev_ip)throws Exception;
	
	public ApiResponseResult getCjbgDepList()throws Exception;
	
	public ApiResponseResult getScdzList(String class_no,String dep_id,String sdata,String edata,String dev_ip)throws Exception;
	
	public ApiResponseResult getZcblList()throws Exception;
	
	public ApiResponseResult getXlpmList()throws Exception;
}
