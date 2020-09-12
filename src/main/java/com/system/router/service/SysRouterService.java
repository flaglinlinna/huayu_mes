package com.system.router.service;


import com.app.base.data.ApiResponseResult;
import com.system.router.entity.SysRouter;

public interface SysRouterService {

    public ApiResponseResult add(SysRouter sysRouter) throws Exception;
    
    public ApiResponseResult edite(SysRouter sysRouter) throws Exception;

    public ApiResponseResult delete(Long id) throws Exception;
    
    public ApiResponseResult getlist(String rolecode,String rolename) throws Exception;
    
    public ApiResponseResult getRolesByUserId(long userId) throws Exception;
    
    public ApiResponseResult getTreeList() throws Exception;


    public ApiResponseResult getRouterTree(String rolecode) throws Exception;
}
