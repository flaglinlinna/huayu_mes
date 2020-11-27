package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.OnlineStaff;

public interface OnlineStaffService {
	
	public ApiResponseResult editMain(OnlineStaff onlineStaff)throws Exception;//修改主表
	
	public ApiResponseResult deleteVice(String taskNo,String viceId)throws Exception;//删除从表数据
	
	public ApiResponseResult deleteMain(String ids)throws Exception;//删除主表数据
	
	public ApiResponseResult getList(String keyword, PageRequest pageRequest)throws Exception;
	
	public ApiResponseResult getMain(Long id)throws Exception;//获取主表单条记录
	
	public ApiResponseResult getMainInfo(Long id)throws Exception;//获取主表单条记录
	
	public ApiResponseResult getClassList()throws Exception;
	
}
