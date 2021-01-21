package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteProductService {
	
	public ApiResponseResult getList(String keyword,String style,String status,String bsCode,String bsType,String bsStatus,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsSimilarProd,
									 String bsPosition ,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,String quoteId,PageRequest pageRequest)throws Exception;//获取列表

	public ApiResponseResult getItemPage(Long id,String bsStyle)throws Exception;//获取待办项数据
	
	//20210119-fyx-审批前校验
	public ApiResponseResult doCheckBefore(String keyword,String quoteId,String bsType)throws Exception;
	
	//20210121-fyx-确认完成后要调用
	public ApiResponseResult doItemFinish(String code,Long quoteId)throws Exception;
}
