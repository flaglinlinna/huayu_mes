package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteBom;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface QuoteBomService {

	//新增BOM 外购件清单列表
	public ApiResponseResult add(QuoteBom quoteBom) throws Exception;

	//编辑BOM 外购件清单列表
	public ApiResponseResult edit(QuoteBom quoteBom) throws Exception;

	//删除BOM 外购件清单列表
	public ApiResponseResult deleteQuoteBom(String ids) throws Exception;

	public ApiResponseResult getBomDetail(Long ids) throws Exception;

	public ApiResponseResult updateRetrial(Long id,String type,Integer value)throws Exception;

	public ApiResponseResult updateBsGroups(Long id,String bsGroups)throws Exception;


	//导出BOM Excel
	public void exportExcel(HttpServletResponse response, Long pkQuote) throws Exception;

	//导入BOM Excel
	public ApiResponseResult doQuoteBomExcel(MultipartFile[] file, Long pkQuote) throws Exception;

	//获取BOM(外购件清单)列表
	public ApiResponseResult getQuoteBomList(String keyword, String pkQuote,String bsElement, PageRequest pageRequest) throws Exception;
	
	//确认完成外购件清单 （修改项目状态加保存界面编辑）
	public ApiResponseResult doStatus(String quoteId,String code,List<QuoteBom> quoteBomList)throws Exception;

	//取消确认完成外购件清单 （修改项目状态）
	public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception;

	public ApiResponseResult editBomList(List<QuoteBom> quoteProcessList) throws Exception;


}
