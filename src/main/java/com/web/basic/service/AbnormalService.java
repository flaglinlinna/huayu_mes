package com.web.basic.service;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Abnormal;
import org.springframework.data.domain.PageRequest;

public interface AbnormalService {
	public ApiResponseResult add(Abnormal abnormal) throws Exception;

	public ApiResponseResult edit(Abnormal abnormal) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getAbnormal(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
