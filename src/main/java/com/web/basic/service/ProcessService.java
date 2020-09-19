package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Process;

public interface ProcessService {
	public ApiResponseResult add(Process process) throws Exception;

	public ApiResponseResult edit(Process process) throws Exception;

	// 根据ID获取
	public ApiResponseResult getProcess(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
