package com.web.produce.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.CardData;


public interface CardDataService {
	public ApiResponseResult add(CardData cardData) throws Exception;

	//public ApiResponseResult edit(CardData cardData) throws Exception;

	// 根据ID获取
	public ApiResponseResult getCardData(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult doStatus(Long id, Integer fstatus) throws Exception;// 卡点是否有效
	
	public ApiResponseResult getEmp() throws Exception;
	
	public ApiResponseResult getDev() throws Exception;
	
	public ApiResponseResult updateData(String devIds,String stype) throws Exception;
	
	public ApiResponseResult updateDataByLine(String line_ids) throws Exception;
}
