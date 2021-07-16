package com.web.project.service;

import com.app.base.data.ApiResponseResult;
import com.web.project.entity.ProjectDuty;
import org.springframework.data.domain.PageRequest;

public interface ProjectDutyService {
	public ApiResponseResult add(ProjectDuty projectDuty) throws Exception;

	public ApiResponseResult edit(ProjectDuty projectDuty) throws Exception;

	// 根据ID获取
//	public ApiResponseResult getProjectDuty(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
