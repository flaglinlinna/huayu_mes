package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface TransitService {

	public ApiResponseResult getProc(String keyword) throws Exception;//获取送验节点列表
	
	public ApiResponseResult getType(String keyword)throws Exception;//获取送检类型列表
	
	public ApiResponseResult checkBarcode(String proc,String ptype,String barcode)throws Exception;//检查 箱号条码-成功执行保存数据，失败返回错误信息
	
	public ApiResponseResult saveData(String proc,String type,String barcode)throws Exception;//保存数据
	
	public ApiResponseResult getHistoryList(String keyword, String hStartTime,String hEndTime,PageRequest pageRequest) throws Exception;
	
}
