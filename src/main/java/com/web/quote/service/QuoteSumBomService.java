package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteBom;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface QuoteSumBomService {

	public ApiResponseResult getList(String quoteId,String keyword,String bsStatus,String bsCode,String bsType,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsProdType,String bsSimilarProd,
									 String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getItemList(Long quoteId,String keyword,PageRequest pageRequest)throws Exception;

	public ApiResponseResult editBsItemCodeReal(Long id,String bsItemCodeReal)throws Exception;

	public void exportExcel(HttpServletResponse response, Long pkQuote) throws Exception;

	//虚拟料号对应导入
	public ApiResponseResult doExcel(MultipartFile[] file, Long pkQuote) throws Exception;

}
