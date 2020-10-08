package com.web.produce.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.Issue;


public interface IssueService {
	public ApiResponseResult add(Issue issue) throws Exception;

	//public ApiResponseResult edit(Issue issue) throws Exception;

	// 根据ID获取
	public ApiResponseResult getIssue(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getEmp() throws Exception;
	public ApiResponseResult getDev() throws Exception;
}
