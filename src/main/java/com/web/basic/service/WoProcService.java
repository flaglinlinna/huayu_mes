package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.WoProc;

public interface WoProcService {
	public ApiResponseResult add(WoProc woProc) throws Exception;

	public ApiResponseResult edit(WoProc woProc) throws Exception;

	// 根据ID获取
	public ApiResponseResult getWoProc(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
