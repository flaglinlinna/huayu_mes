package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.SchedulingMain;
import org.springframework.data.domain.PageRequest;


import javax.servlet.http.HttpServletResponse;

/**
 * 排产信息 新
 */
public interface SchedulingMainService {

    public ApiResponseResult add(SchedulingMain scheduling) throws Exception;

    public ApiResponseResult edit(SchedulingMain scheduling) throws Exception;

    public ApiResponseResult delete(Long id) throws Exception;

    public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

    public ApiResponseResult getDeptSelect() throws Exception;

}
