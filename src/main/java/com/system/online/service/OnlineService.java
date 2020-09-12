package com.system.online.service;


import com.app.base.data.ApiResponseResult;
import com.system.log.entity.SysLog;
import org.springframework.data.domain.PageRequest;

public interface OnlineService {

    public ApiResponseResult getlist(String keyword, PageRequest pageRequest) throws Exception;

    public ApiResponseResult delete(String sessionId) throws Exception;
}
