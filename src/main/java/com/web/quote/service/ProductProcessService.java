package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductProcess;
import com.web.quote.entity.QuoteBom;
import com.web.quote.entity.QuoteProcess;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProductProcessService {

	public ApiResponseResult add(ProductProcess productProcess)throws Exception;

	public ApiResponseResult edit(ProductProcess productProcess)throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doStatus(Long quoteId,String bsType,String bsCode) throws Exception;

	public ApiResponseResult cancelStatus(Long quoteId,String bsType,String bsCode) throws Exception;

	public ApiResponseResult updateModelType(Long quoteId,String bsModelType) throws Exception;

	public ApiResponseResult doExcel(MultipartFile[] file, String bsType, Long quoteId) throws Exception;

	public void exportExcel(HttpServletResponse response, String bsType, Long quoteId) throws Exception;

	public ApiResponseResult getList(String keyword, String bsType, String quoteId, PageRequest pageRequest) throws Exception;

    public ApiResponseResult getListByPkQuote(Long pkQuote, PageRequest pageRequest) throws Exception;

	public ApiResponseResult getBomSelect(String pkQuote) throws Exception;

	public ApiResponseResult uploadCheck(Long pkQuote,String bsType) throws  Exception;
	
	//20210119-fyx-根据大类过滤工序
	public ApiResponseResult getProcListByType(String bsType) throws  Exception;

	public ApiResponseResult delFile(Long id,Long fileId) throws Exception;

	public ApiResponseResult editProcessList(List<ProductProcess> productProcessList) throws Exception;


}
