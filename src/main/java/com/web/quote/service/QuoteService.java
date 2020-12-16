package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;

public interface QuoteService {

	public ApiResponseResult add(Quote quote)throws Exception;
}
