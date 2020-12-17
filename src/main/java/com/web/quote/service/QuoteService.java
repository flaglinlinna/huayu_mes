package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;

public interface QuoteService {

	public ApiResponseResult add(Quote quote)throws Exception;
	
	public ApiResponseResult getProfitProd()throws Exception;//获取产品利润率维护表
	
	public ApiResponseResult getList(String keyword,PageRequest pageRequest)throws Exception;//获取报价单列表
}
