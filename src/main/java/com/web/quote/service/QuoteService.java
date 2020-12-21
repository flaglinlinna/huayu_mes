package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteService {

	public ApiResponseResult add(Quote quote)throws Exception;
	
	public ApiResponseResult getProfitProd()throws Exception;//获取产品利润率维护表
	
	public ApiResponseResult getList(String keyword,String status,PageRequest pageRequest)throws Exception;//获取报价单列表

	public ApiResponseResult edit(Quote quote)throws Exception;
	
	public ApiResponseResult getSingle(Long id)throws Exception;//获取单张报价单
	
	public ApiResponseResult getItemPage(Long id,String bsStyle)throws Exception;//获取待办项数据
	
	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;//变更报价单状态

	
	public ApiResponseResult doItemFinish(String code,Long quoteId)throws Exception;//20201219-fyx-确认完成后要调用
}
