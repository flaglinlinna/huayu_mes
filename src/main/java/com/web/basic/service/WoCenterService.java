package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.WoCenter;

public interface WoCenterService {
	public ApiResponseResult add(WoCenter woCenter) throws Exception;

	public ApiResponseResult edit(WoCenter woCenter) throws Exception;

	// 根据ID获取
	public ApiResponseResult getWoCenter(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
