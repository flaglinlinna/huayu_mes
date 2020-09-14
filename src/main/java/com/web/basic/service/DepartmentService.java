package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Department;

public interface DepartmentService {
	public ApiResponseResult add(Department department) throws Exception;

	public ApiResponseResult edit(Department department) throws Exception;

	 //根据ID获取部门
    public ApiResponseResult getDepart(Long id) throws Exception;
	
	public ApiResponseResult delete(Long id) throws Exception;
	
	 public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;//状态改变

	public ApiResponseResult getList( String keyword , PageRequest pageRequest) throws Exception;
}
