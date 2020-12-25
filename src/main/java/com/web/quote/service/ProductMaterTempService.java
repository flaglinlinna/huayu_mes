package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMaterTemp;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductMaterTempService {

	public ApiResponseResult edit(ProductMaterTemp productMater)throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	//制造部材料 导入临时表
	public ApiResponseResult doExcel(MultipartFile[] file, String bsType, Long quoteId) throws Exception;

	//采购填报价格 导入临时表
	public ApiResponseResult doExcel(MultipartFile[] file,Long quoteId) throws Exception;

	// 临时表列表
	public ApiResponseResult getList(String bsPurchase, String bsType, String quoteId, PageRequest pageRequest) throws Exception;

	//制造部材料 确定导入正式表
//	public ApiResponseResult importByTemp(String quoteId,String bsType) throws Exception;

	//采购填报价格 确定导入正式表
	public ApiResponseResult importByTemp(Long quoteId) throws Exception;
}
