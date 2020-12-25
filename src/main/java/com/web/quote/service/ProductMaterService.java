package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductMaterService {

	public ApiResponseResult add(ProductMater productMater)throws Exception;

	public ApiResponseResult edit(ProductMater productMater)throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult Confirm(Long quoteId,String bsType) throws Exception;

	public ApiResponseResult doExcel(MultipartFile[] file,String bsType,Long quoteId) throws Exception;

	public ApiResponseResult getList(String keyword,String bsType, String quoteId,PageRequest pageRequest) throws Exception;

    public ApiResponseResult getListByPkQuote(Long pkQuote, PageRequest pageRequest) throws Exception;
    
    //20201222-fyx-计算报价汇总
    public ApiResponseResult doSumFee(Long pkQuote) throws Exception;
}
