package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteAllService {
	
	public ApiResponseResult getList(String keyword,String style,String status,String bsCode,String bsType,String bsStatus,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsProdType,String bsSimilarProd,
									 String bsPosition ,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,String quoteId,PageRequest pageRequest)throws Exception;//获取报价单列表

}
