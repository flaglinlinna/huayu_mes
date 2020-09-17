package com.web.basic.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ProdProc;

public interface ProdProcService {
	/**
	 * 操作主表
	 * */
//	public ApiResponseResult add(ProdProc prodProc) throws Exception;
//
//	public ApiResponseResult edit(ProdProc prodProc) throws Exception;

	// 根据ID获取
	public ApiResponseResult getProdProc(Long id) throws Exception;
	
//	public ApiResponseResult delete(Long id) throws Exception;
//
//	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	public ApiResponseResult getData() throws Exception;
	//获取附表信息
	public ApiResponseResult getDetailList(String keyword, PageRequest pageRequest) throws Exception;
}
