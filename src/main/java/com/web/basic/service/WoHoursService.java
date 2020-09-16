package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.WoHours;

public interface WoHoursService {
	public ApiResponseResult add(WoHours woHours) throws Exception;

	public ApiResponseResult edit(WoHours woHours) throws Exception;
	// 根据ID获取
	public ApiResponseResult getWoHours(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
