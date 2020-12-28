package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteBom;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface QuoteBomService {

	//新增BOM 外购件清单列表
	public ApiResponseResult add(QuoteBom quoteBom) throws Exception;

	//编辑BOM 外购件清单列表
	public ApiResponseResult edit(QuoteBom quoteBom) throws Exception;

	//删除BOM 外购件清单列表
	public ApiResponseResult deleteQuoteBom(Long id) throws Exception;

	public void exportExcel(HttpServletResponse response, Long pkQuote) throws Exception;

	//导入BOM Excel
	public ApiResponseResult doQuoteBomExcel(MultipartFile[] file, Long pkQuote) throws Exception;

	//获取BOM(外购件清单)列表
	public ApiResponseResult getQuoteBomList(String keyword, String pkQuote, PageRequest pageRequest) throws Exception;
	
	//确认完成外购件清单 （修改项目状态）
	public ApiResponseResult doStatus(String quoteId,String code)throws Exception;

}
