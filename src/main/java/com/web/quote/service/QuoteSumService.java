package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface QuoteSumService {

	public ApiResponseResult getList(String quoteId,String keyword,String bsStatus,String bsCode,String bsType,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsProdType,String bsSimilarProd,
									 String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getQuoteList(String keyword,String quoteId,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getSumByQuote(String quoteId)throws Exception;

	public ApiResponseResult countMeterAndProcess(String quoteId)throws Exception;

	public ApiResponseResult updateProfitNet(long quoteId, BigDecimal profitNet)throws Exception;

	public ApiResponseResult updateBsManageFee(long quoteId, BigDecimal bsManageFee)throws Exception;

	public ApiResponseResult getQuoteBomByQuote(String quoteId)throws Exception;//查询总费用
	
	public ApiResponseResult countQuoteTreeBom(Long quoteId) throws Exception;//计算总费用
	
	public ApiResponseResult setBade(Long quoteId,Integer bsBade) throws Exception;//设置中标
}
