package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductFileService {

	//新增产品资料文件
	public ApiResponseResult add(ProductFile productFile) throws Exception;

	//删除产品资料文件
//	public ApiResponseResult edit(QuoteBom quoteBom) throws Exception;

	//删除产品资料文件
	public ApiResponseResult delete(Long id) throws Exception;

	//获取BOM(外购件清单)列表
	public ApiResponseResult getList(String keyword, String pkQuote, PageRequest pageRequest) throws Exception;

}
