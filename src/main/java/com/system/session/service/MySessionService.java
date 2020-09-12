package com.system.session.service;


import com.app.base.data.ApiResponseResult;
import com.system.log.entity.SysLog;
import org.springframework.data.domain.PageRequest;

public interface MySessionService {
	
	public ApiResponseResult getlist(String keyword, PageRequest pageRequest);
	
}
