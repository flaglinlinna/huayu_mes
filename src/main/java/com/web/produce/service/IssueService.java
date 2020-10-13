package com.web.produce.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.Issue;


public interface IssueService {
	
	public ApiResponseResult add(String devList,String empList) throws Exception;
	
	public ApiResponseResult clear(String devList,String empList) throws Exception;
	
	public ApiResponseResult getIssue(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getEmp(String empKeyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getDev(String devKeyword, PageRequest pageRequest) throws Exception;
}
