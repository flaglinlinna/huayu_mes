package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.SchedulingTemp;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 排产导入临时信息
 */
public interface SchedulingTempService {

    public ApiResponseResult edit(SchedulingTemp temp) throws Exception;

    public ApiResponseResult delete(String ids) throws Exception;

    public ApiResponseResult getList(String keyword, String startTime, String endTime, PageRequest pageRequest) throws Exception;

    public ApiResponseResult getTempData(Long id) throws Exception;

    public ApiResponseResult getExcel(HttpServletResponse response) throws Exception;

    public ApiResponseResult doExcel(MultipartFile file) throws Exception;

    public ApiResponseResult doCheckProc(String ids) throws Exception;

    public ApiResponseResult doEffect(String ids) throws Exception;
}
