package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteBom;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteBomService {

	//删除BOM 外购件清单列表
	public ApiResponseResult deleteQuoteBom(Long id) throws Exception;

	//导入BOM Excel
	public ApiResponseResult doQuoteBomExcel(MultipartFile[] file, Long pkQuote) throws Exception;

	//获取BOM(外购件清单)列表
	public ApiResponseResult getQuoteBomList(String keyword, Long pkQuote, PageRequest pageRequest) throws Exception;

}
