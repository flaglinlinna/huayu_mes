package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Employee;

public interface EmployeeService {
	public ApiResponseResult add(Employee employee) throws Exception;

	public ApiResponseResult edit(Employee employee) throws Exception;

	// 根据ID获取
	public ApiResponseResult getEmployee(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;
	
	public ApiResponseResult doStatus(Long id, Integer empStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword,String empStatus, PageRequest pageRequest) throws Exception;
}
