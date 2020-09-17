package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Client;

public interface ClientService {
	public ApiResponseResult add(Client client) throws Exception;

	public ApiResponseResult edit(Client client) throws Exception;

	// 根据ID获取
	public ApiResponseResult getClient(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
