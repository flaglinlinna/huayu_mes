package com.web.kanban.service;

import com.app.base.data.ApiResponseResult;

import java.util.List;

import org.springframework.boot.security.servlet.ApplicationContextRequestMatcher;
import org.springframework.data.domain.PageRequest;

public interface KanbanService {

	public ApiResponseResult getCjbgList(String class_no,String dep_id,String sdata,String dev_ip)throws Exception;
	
	public ApiResponseResult getCjbgDepList()throws Exception;
	
	public ApiResponseResult getLiner()throws Exception;
	
	public ApiResponseResult getScdzList(String class_no,String dep_id,String sdata,String dev_ip)throws Exception;
	
	public ApiResponseResult getZcblDepList()throws Exception;
	
	public ApiResponseResult getZcblList(String class_no, String dep_id, String sdata,String dev_ip)throws Exception;
	
	public ApiResponseResult getXlpmList(String class_id,String dep_id, String sdata, String dev_ip,String liner)throws Exception;

	public ApiResponseResult getDfgList(String class_id,String dep_id, String sdata,String usr_id,String dev_ip)throws Exception;
	
	//获取产线电子看板
	public ApiResponseResult getCxdzList(String class_id,String dep_id, String sdata, String dev_ip,String liner)throws Exception;
}
