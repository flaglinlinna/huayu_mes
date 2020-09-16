package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ChkBad;

public interface ChkBadService {
	public ApiResponseResult add(ChkBad chkBad) throws Exception;

	public ApiResponseResult edit(ChkBad chkBad) throws Exception;

	// 根据ID获取
	public ApiResponseResult getChkBad(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
