package com.web.basePrice.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteItemBase;
import org.springframework.data.domain.PageRequest;

public interface QuoteItemBaseService {

	public ApiResponseResult getList(String keyword, PageRequest pageRequest)throws Exception;//获取报价单列表

	public ApiResponseResult edit(QuoteItemBase quoteItemBase)throws Exception;

}
