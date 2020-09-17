package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ChkBadDet;

public interface ChkBadDetService {
	public ApiResponseResult add(ChkBadDet chkBadDet) throws Exception;

	public ApiResponseResult edit(ChkBadDet chkBadDet) throws Exception;

	// 根据ID获取
	public ApiResponseResult getChkBadDet(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getChkBadList() throws Exception;
}
