package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteProductService {
	
	public ApiResponseResult getList(String keyword,String style,String status,PageRequest pageRequest)throws Exception;//获取列表

	public ApiResponseResult getItemPage(Long id,String bsStyle)throws Exception;//获取待办项数据
}
