package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteService {

	public ApiResponseResult add(Quote quote)throws Exception;
	
	public ApiResponseResult getProfitProd()throws Exception;//获取产品利润率维护表
	
	public ApiResponseResult getList(String keyword,PageRequest pageRequest)throws Exception;//获取报价单列表

	public ApiResponseResult edit(Quote quote)throws Exception;
	
	public ApiResponseResult getSingle(Long id)throws Exception;//获取单张报价单
	
	public ApiResponseResult getItemPage(Long id)throws Exception;//获取待办项数据
	
	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;//变更报价单状态

	//删除BOM 外购件清单列表
	public ApiResponseResult deleteQuoteBom(Long id) throws Exception;

	//导入BOM Excel
	public ApiResponseResult doQuoteBomExcel(MultipartFile[] file, Long pkQuote) throws Exception;

	//获取BOM(外购件清单)列表
	public ApiResponseResult getQuoteBomList(String keyword,Long pkQuote,PageRequest pageRequest) throws Exception;

}
