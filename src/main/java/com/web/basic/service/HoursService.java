package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Hours;

public interface HoursService {
	public ApiResponseResult add(Hours hours) throws Exception;

	public ApiResponseResult edit(Hours hours) throws Exception;
	// 根据ID获取
	public ApiResponseResult getHours(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
