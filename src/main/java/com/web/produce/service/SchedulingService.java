package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.Scheduling;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 排产信息
 */
public interface SchedulingService {

    public ApiResponseResult add(Scheduling scheduling) throws Exception;

    public ApiResponseResult edit(Scheduling scheduling) throws Exception;

    public ApiResponseResult delete(Long id) throws Exception;

    public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

    public ApiResponseResult getExcel(HttpServletResponse response) throws Exception;
}
