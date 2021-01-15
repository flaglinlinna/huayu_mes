package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface PurchaseService {
	
	public ApiResponseResult getList(String keyword,String bsStatus,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getQuoteList(String keyword,String quoteId,PageRequest pageRequest) throws Exception;

	public ApiResponseResult edit(ProductMater productMater) throws Exception;

	public ApiResponseResult getStatus(Long pkQuote,Integer bsStatusPurchase) throws Exception;

	public void exportExcel(HttpServletResponse response, Long quoteId) throws Exception;

	public ApiResponseResult doExcel(MultipartFile[] file, Long quoteId) throws Exception;

	public ApiResponseResult doStatus(Long quoteId) throws Exception;//确认完成
	
	public ApiResponseResult doSumHouLoss(Long quoteId) throws Exception;//计算后工序损耗
	
	public ApiResponseResult doGear(String id,String gear,String price) throws Exception;//添加价格档位
	
	public ApiResponseResult doCheckBefore(String keyword,String quoteId) throws Exception;//采购部发起审批前校验
	
}
