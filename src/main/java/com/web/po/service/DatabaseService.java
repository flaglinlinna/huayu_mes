package com.web.po.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.po.entity.DatabaseInfo;


public interface DatabaseService {
	
	ApiResponseResult testConnection(String type,String url,String username,String password)throws Exception;
	
	ApiResponseResult getlist(String string, PageRequest pageRequest)throws Exception;
	
	ApiResponseResult add(DatabaseInfo databaseInfo)throws Exception;
}
