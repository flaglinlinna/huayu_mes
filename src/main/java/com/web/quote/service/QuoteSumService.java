package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface QuoteSumService {
	
	public ApiResponseResult getList(String keyword,String bsStatus,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getQuoteList(String keyword,String quoteId,PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getSumByQuote(String quoteId)throws Exception;
	
	public ApiResponseResult countMeterAndProcess(String quoteId)throws Exception;

	public ApiResponseResult updateProfitNet(long quoteId, BigDecimal profitNet)throws Exception;
}
