package com.web.po.service;

import com.app.base.data.ApiResponseResult;
import com.web.po.entity.Interfaces;
import com.web.po.entity.InterfacesRequest;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

/**
 * 接口信息配置表
 *
 */
public interface InterfacesService {

    public ApiResponseResult add(Interfaces interfaces) throws Exception;

    public ApiResponseResult edit(Interfaces interfaces) throws Exception;

    public ApiResponseResult delete(Long id) throws Exception;

    public ApiResponseResult getList(String keyword, String bsCode, String bsName, Integer bsStatus, Date createdTimeStart, Date createdTimeEnd, PageRequest pageRequest) throws Exception;

    //根据ID获取接口配置
    public ApiResponseResult getInterfaces(Long id) throws Exception;

    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;

    public ApiResponseResult addRequest(InterfacesRequest request) throws Exception;

    public ApiResponseResult editRequest(InterfacesRequest request) throws Exception;

    public ApiResponseResult deleteRequest(Long id) throws Exception;

    public ApiResponseResult getRequestList(String keyword, Long interId, PageRequest pageRequest) throws Exception;

    //根据ID获取请求参数
    public ApiResponseResult getRequest(Long id) throws Exception;

    public ApiResponseResult deleteRequestAll(String idsStr) throws Exception;
}
