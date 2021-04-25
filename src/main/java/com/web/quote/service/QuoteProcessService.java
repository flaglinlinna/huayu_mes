package com.web.quote.service;

import com.web.quote.entity.QuoteProcess;
import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuoteProcessService {

   public ApiResponseResult getAddList(Long workcenterId,PageRequest pageRequest)  throws Exception;
	
   public ApiResponseResult getBomList(String keyword,Long quoteId, PageRequest pageRequest)  throws Exception;//获取物料列表
	
   public ApiResponseResult getBomList2(String quoteId)throws Exception;
	
   public ApiResponseResult add(String proc,String itemId,String quoteId,String bsElement)  throws Exception;
	
   public ApiResponseResult getList(String keyword,String pkQuote, PageRequest pageRequest)  throws Exception;
	
   public ApiResponseResult delete(String ids)  throws Exception;

   public ApiResponseResult doProcOrder(Long id,Integer bsOrder)  throws Exception;

   public ApiResponseResult doFmemo(Long id,String  fmemo)throws Exception;

   public ApiResponseResult doBsMaterName(Long id,Long  bomId)throws Exception;

   public ApiResponseResult doProc(Long id,Long  procId)throws Exception;

   public ApiResponseResult doBsGroups(Long id,String  bsGroups)throws Exception;

   public ApiResponseResult doStatus(String quoteId,String code)throws Exception;

   public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception;
   
   public ApiResponseResult getListByQuoteAndName(String quoteId,String name)throws Exception;

   public ApiResponseResult addProcessByBom(Long quoteId);

   public ApiResponseResult editProcessList(List<QuoteProcess> quoteProcessList) throws Exception;
   
}
