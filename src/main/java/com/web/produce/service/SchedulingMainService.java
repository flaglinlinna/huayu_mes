package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.SchedulingDet;
import com.web.produce.entity.SchedulingMain;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;


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

    public ApiResponseResult getSchedulingMain(String id) throws Exception;

    public ApiResponseResult doStatus(Long id, Integer status) throws Exception;

    public ApiResponseResult getDetList(String keyword, Long mid, String startTime, String endTime, PageRequest pageRequest) throws Exception;

    public ApiResponseResult doExcel(MultipartFile file, Long mid) throws Exception;

    public ApiResponseResult doCheckProc(String ids) throws Exception;

    public ApiResponseResult deleteDet(String ids) throws Exception;

    public ApiResponseResult doEffect(String ids) throws Exception;

    public ApiResponseResult editDet(SchedulingDet schedulingDet) throws Exception;

    public ApiResponseResult getDet(String id) throws Exception;
}
