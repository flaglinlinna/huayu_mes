package com.web.produce.service;

import com.app.base.data.ApiResponseResult;

public interface CheckCodeService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;

	public ApiResponseResult subCode(String taskNo, String barcode1, String barcode2) throws Exception;
}
