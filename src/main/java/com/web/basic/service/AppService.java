package com.web.basic.service;


import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.AppVersion;

public interface AppService {
	public ApiResponseResult add(AppVersion app) throws Exception;

	public ApiResponseResult edit(AppVersion app) throws Exception;

	public ApiResponseResult delete(String ids) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult upload(MultipartFile file)throws Exception;
}
