package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.OnlineStaff;

public interface SwitchStaffService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;// 获取旧制令单
	
	public ApiResponseResult getNewTaskNo(String keyword) throws Exception;// 获取新制令单

	public ApiResponseResult getTaskNoEmp(String aff_id, PageRequest pageRequest) throws Exception;// 获取待调整人员

	public ApiResponseResult getLine(String keyword) throws Exception;

	public ApiResponseResult getClassType() throws Exception;

	public ApiResponseResult doSwitch(String lastTaskNo_id,String lastDatetimeEnd,
			String newTaskNo, String newLineId,String newHourType, String newClassId,
			String newDatetimeBegin, String empList,PageRequest pageRequest) throws Exception;

}
