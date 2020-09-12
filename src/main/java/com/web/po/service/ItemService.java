package com.web.po.service;

import com.app.base.data.ApiResponseResult;

import java.util.List;

import org.springframework.data.domain.PageRequest;

public interface ItemService {
	ApiResponseResult findForecastList(String string,int page,int rows)throws Exception;
}
