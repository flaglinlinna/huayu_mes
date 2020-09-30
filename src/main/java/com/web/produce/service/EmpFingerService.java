package com.web.produce.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.EmpFinger;


public interface EmpFingerService {
	public ApiResponseResult add(EmpFinger empFinger) throws Exception;

	public ApiResponseResult edit(EmpFinger empFinger) throws Exception;

	// 根据ID获取
	public ApiResponseResult getEmpFinger(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getEmpList() throws Exception;//获取人员信息
	
	//public ApiResponseResult doEnabled(Long id, Integer enabled) throws Exception;// 是否有效
}
