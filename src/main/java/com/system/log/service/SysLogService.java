package com.system.log.service;


import com.app.base.data.ApiResponseResult;
import com.system.log.entity.SysLog;
import org.springframework.data.domain.PageRequest;

public interface SysLogService {

    public ApiResponseResult add(SysLog sysLog) throws Exception;

    public ApiResponseResult getlist(String keyword, PageRequest pageRequest) throws Exception;
    
    public ApiResponseResult debug(String method,String methodName) ;
    
    public ApiResponseResult success(String method,String methodName,Object param) ;
    
    public ApiResponseResult error(String method,String methodName,Object param) ;
    
    public ApiResponseResult login(String method,String methodName,String param) ;

}
