package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteMouldService {

  // public ApiResponseResult getMouldList()  throws Exception;//获取模具清单
	
  // public ApiResponseResult getBomList(String keyword, PageRequest pageRequest)  throws Exception;//获取物料列表
	
   //public ApiResponseResult add(String proc,String itemId,String quoteId)  throws Exception;
	
   public ApiResponseResult getList(String keyword,String pkQuote, PageRequest pageRequest)  throws Exception;
	
  // public ApiResponseResult delete(Long id)  throws Exception;
	
   //public ApiResponseResult doActQuote(Long id,Integer bsActQuote)  throws Exception;
   
   //public ApiResponseResult doStatus(String quoteId,String code)throws Exception;
}
