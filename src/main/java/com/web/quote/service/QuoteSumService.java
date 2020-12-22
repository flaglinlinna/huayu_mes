package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteSumService {
	
	public ApiResponseResult getList(String keyword,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getQuoteList(String keyword,String quoteId,PageRequest pageRequest) throws Exception;
}
