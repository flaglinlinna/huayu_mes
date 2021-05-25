package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProductMaterService {

	public ApiResponseResult add(ProductMater productMater)throws Exception;

	public ApiResponseResult edit(ProductMater productMater)throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult updateUnit(Long id,Long unitId) throws Exception;

	public ApiResponseResult doStatus(Long quoteId,String bsType,String bsCode,List<ProductMater> productMaterList) throws Exception;

	public ApiResponseResult cancelStatus(Long quoteId,String bsType,String bsCode) throws Exception;

	public ApiResponseResult getBomSelect(String pkQuote) throws Exception;

	public ApiResponseResult doExcel(MultipartFile[] file,String bsType,Long quoteId) throws Exception;

	public ApiResponseResult getList(String keyword,String bsType, String quoteId,String bsAgent,PageRequest pageRequest) throws Exception;

    public ApiResponseResult getListByPkQuote(Long pkQuote, PageRequest pageRequest) throws Exception;

	public void exportExcel(HttpServletResponse response, String bsType, Long quoteId) throws Exception;
    
    //20201222-fyx-计算报价汇总
    public ApiResponseResult doSumFee(Long pkQuote) throws Exception;

	public ApiResponseResult editMaterList(List<ProductMater> productMaterList) throws Exception;
}
