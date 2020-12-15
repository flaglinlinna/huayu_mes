package com.web.basic.service;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.SysParamSub;
import org.springframework.data.domain.PageRequest;

public interface SysParamSubService {
	public ApiResponseResult add(SysParamSub sysParamSub) throws Exception;

	public ApiResponseResult edit(SysParamSub sysParamSub) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(Long mid, PageRequest pageRequest) throws Exception;

}
