package com.web.kanban.service;

import com.app.base.data.ApiResponseResult;

import java.util.List;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.boot.security.servlet.ApplicationContextRequestMatcher;
import org.springframework.data.domain.PageRequest;

public interface KanbanService {
	//获取车间看板
	public ApiResponseResult getCjbgList(String class_no,String dep_id,String sdata,String dev_ip)throws Exception;
	
	//获取部门1
	public ApiResponseResult getCjbgDepList()throws Exception;
	
	//获取线长信息
	public ApiResponseResult getLiner()throws Exception;
	
	//获取生产电子看板
	public ApiResponseResult getScdzList(String class_no,String dep_id,String sdata,String dev_ip)throws Exception;
	
	//获取部门2
	public ApiResponseResult getZcblDepList()throws Exception;
	
	//获取制程不良看板
	public ApiResponseResult getZcblList(String class_no, String dep_id, String sdata,String dev_ip)throws Exception;
	
	//获取效率排名看板
	public ApiResponseResult getXlpmList(String class_id,String dep_id, String sdata, String dev_ip,String liner)throws Exception;

	//获取待返工看板
	public ApiResponseResult getDfgList(String class_id,String dep_id, String sdata,String usr_id,String dev_ip)throws Exception;
	
	//获取产线电子看板
	public ApiResponseResult getCxdzList(String class_id,String dep_id, String sdata, String dev_ip,String liner)throws Exception;
	
	//获取产线生产看板
	public ApiResponseResult getCxscList(String taskNo,String deptId,String liner,String dev_ip,String interval)throws Exception;

	//获取刷新间隔时间
	public ApiResponseResult getIntervalTime()throws Exception;
	
}
