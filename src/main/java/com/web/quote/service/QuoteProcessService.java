package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteProcessService {

//ApiResponseResult getAddList()  throws Exception;
	
	//ApiResponseResult getBomList(String keyword, PageRequest pageRequest)  throws Exception;//获取物料列表
	
	//ApiResponseResult add(String proc,String itemIds,String itemNos)  throws Exception;
	
	ApiResponseResult getList(String keyword, PageRequest pageRequest)  throws Exception;
	
	//ApiResponseResult delete(Long id)  throws Exception;
	
	//ApiResponseResult doProcOrder(Long id,Integer procOrder)  throws Exception;
}
