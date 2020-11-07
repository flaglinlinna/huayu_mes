package com.web.basic.service;


import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Line;

public interface LineService {
	public ApiResponseResult add(Line line) throws Exception;

	public ApiResponseResult edit(Line line) throws Exception;

	// 根据ID获取
	public ApiResponseResult getLine(Long id) throws Exception;

	public ApiResponseResult delete(String ids) throws Exception;

	public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword,String lineNo,String linerName,String lastupdateDate,
    		String checkStatus,String createDate,String linerCode,String lineName, PageRequest pageRequest) throws Exception;
}
