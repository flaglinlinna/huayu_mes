package com.web.report.service;

import com.app.base.data.ApiResponseResult;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;

public interface RptHourService {

	public ApiResponseResult getDeptInfo(String keyword) throws Exception;

	public ApiResponseResult getReport(String beginTime, String endTime, String itemNo, String linerName, String taskNo, PageRequest pageRequest) throws Exception;

	public void exportExcel(HttpServletResponse response,String beginTime, String endTime, String itemNo, String linerName, String taskNo, PageRequest pageRequest) throws Exception;

}
