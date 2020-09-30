package com.web.basic.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface ProdProcService {

	ApiResponseResult getAddList()  throws Exception;
	
	ApiResponseResult getProdList(String keyword, PageRequest pageRequest)  throws Exception;
	
	ApiResponseResult add(String proc,String itemIds,String itemNos)  throws Exception;
	
	ApiResponseResult getList(String keyword, PageRequest pageRequest)  throws Exception;
	
	ApiResponseResult delete(Long id)  throws Exception;
	
	ApiResponseResult doJobAttr(Long id,Integer jobAttr)  throws Exception;
}
