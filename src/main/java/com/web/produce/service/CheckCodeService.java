package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

import javax.servlet.http.HttpServletResponse;

public interface CheckCodeService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;

	public ApiResponseResult getLiner(String keyword,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getItemCode(String keyword,PageRequest pageRequest)throws Exception;

	public ApiResponseResult subCode(String taskNo,String itemCode,String linerName, String barcode1, String barcode2,String checkRep,String type,String prcType) throws Exception;

	public ApiResponseResult updateCode(String company,String factory,String userId,String taskNo,String itemCode,String linerName, String type,String time,String prcType) throws Exception;
	
	public ApiResponseResult getHistoryList(String keyword,Integer errFlag, String hStartTime,String hEndTime, String scanType, String scanFrom,PageRequest pageRequest) throws Exception;

	public void exportExcel(HttpServletResponse response,String keyword, Integer errFlag, String hStartTime, String hEndTime, String scanType, String scanFrom, PageRequest pageRequest) throws Exception;

}
