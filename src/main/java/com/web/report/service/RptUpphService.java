package com.web.report.service;

import com.app.base.data.ApiResponseResult;

public interface RptUpphService {

	public ApiResponseResult getDeptInfo(String keyword) throws Exception;

	public ApiResponseResult getReport(String beginTime, String endTime, String keyword) throws Exception;

}
