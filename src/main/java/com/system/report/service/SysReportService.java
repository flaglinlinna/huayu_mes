package com.system.report.service;

import com.app.base.data.ApiResponseResult;

import org.springframework.data.domain.PageRequest;

public interface SysReportService {

	ApiResponseResult getlist(String string, PageRequest pageRequest)throws Exception;
    
}
