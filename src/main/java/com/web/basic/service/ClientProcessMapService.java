package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ClientProcessMap;

public interface ClientProcessMapService {
	//public ApiResponseResult add(ClientProcessMap clientProcess) throws Exception;
	public ApiResponseResult addItem(String procIdList,Long clientId) throws Exception;
	//public ApiResponseResult edit(ClientProcessMap clientProcess) throws Exception;

	// 根据ID获取记录
	public ApiResponseResult getClientItem(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	public ApiResponseResult getProcList()throws Exception;
}
