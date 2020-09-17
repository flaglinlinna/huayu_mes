package com.web.report.service;

import com.app.base.data.ApiResponseResult;

import org.springframework.data.domain.PageRequest;

public interface CommonService {

	ApiResponseResult getlist(String string, PageRequest pageRequest)throws Exception;
    
}
