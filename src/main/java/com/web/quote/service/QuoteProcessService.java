package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteProcessService {

   public ApiResponseResult getAddList()  throws Exception;
	
   public ApiResponseResult getBomList(String keyword, PageRequest pageRequest)  throws Exception;//获取物料列表
	
   public ApiResponseResult getBomList2(String quoteId)throws Exception;
	
   public ApiResponseResult add(String proc,String itemId,String quoteId)  throws Exception;
	
   public ApiResponseResult getList(String keyword, PageRequest pageRequest)  throws Exception;
	
   public ApiResponseResult delete(Long id)  throws Exception;
	
   public ApiResponseResult doProcOrder(Long id,Integer bsOrder)  throws Exception;
}
