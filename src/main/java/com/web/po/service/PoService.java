package com.web.po.service;

import com.app.base.data.ApiResponseResult;

import java.util.List;

import org.springframework.data.domain.PageRequest;

public interface PoService {

	ApiResponseResult findProductionPOBoardData(String string)throws Exception;

	ApiResponseResult findPoLineList(String string,int page,int rows)throws Exception;
}
