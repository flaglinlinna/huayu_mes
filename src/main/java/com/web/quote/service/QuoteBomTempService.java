package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteBom;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteBomTempService {

	//外购件清单临时表列表
	public ApiResponseResult getList(String keyword,String pkQuote,PageRequest pageRequest) throws Exception;

	//导入excel到临时表
	public ApiResponseResult doExcel(MultipartFile[] file, Long pkQuote) throws Exception;

//	//临时表导入正式表
	public ApiResponseResult importByTemp(Long pkQuote) throws Exception;



}
