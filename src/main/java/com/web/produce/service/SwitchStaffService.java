package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.OnlineStaff;

public interface SwitchStaffService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;// 获取制令单

	public ApiResponseResult getTaskNoEmp(String taskNo, String workDate, PageRequest pageRequest) throws Exception;// 获取待调整人员

	public ApiResponseResult getLine(String keyword) throws Exception;

	public ApiResponseResult getClassType() throws Exception;

	public ApiResponseResult doSwitch(String lastTaskNo, String lastLineId, String lastHourType, 
			String lastClassId,String lastWorkDate, String lastDateEnd, String lastTimeEnd,
			String newTaskNo, String newLineId,String newHourType, String newClassId,
			String newWorkDate, String newTimeBegin, String empList,PageRequest pageRequest) throws Exception;

}
