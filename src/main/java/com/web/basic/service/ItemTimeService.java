package com.web.basic.service;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ItemsTime;
import org.springframework.data.domain.PageRequest;

public interface ItemTimeService {
	public ApiResponseResult add(ItemsTime itemsTime) throws Exception;

	public ApiResponseResult edit(ItemsTime itemsTime) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getItemSelect(String keyword,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
