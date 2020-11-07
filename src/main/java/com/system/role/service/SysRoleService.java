package com.system.role.service;


import com.app.base.data.ApiResponseResult;
import com.system.role.entity.SysRole;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

public interface SysRoleService {

    public ApiResponseResult add(SysRole sysRole) throws Exception;
    
    public ApiResponseResult edit(SysRole sysRole) throws Exception;

    public ApiResponseResult delete(Long id) throws Exception;
    
    public ApiResponseResult getList(String keyword, String bsCode, String bsName, Date createdTimeStart, Date createdTimeEnd, Integer bsStatus, PageRequest pageRequest) throws Exception;
    
    public ApiResponseResult getCheckedRoles(long userId) throws Exception;
    
    public ApiResponseResult saveUserRoles(long userId,String roles) throws Exception;
    
    public ApiResponseResult addRouter(String rolecode,String roles) throws Exception;
    
    public ApiResponseResult getRouter(String rolecode) throws Exception;

    //获取当前角色的操作权限
    public ApiResponseResult getPermission() throws Exception;

    //根据ID获取角色
    public ApiResponseResult getRole(Long id) throws Exception;

    //获取所有角色
    public ApiResponseResult getRoles() throws Exception;

    //获取所有角色
    public ApiResponseResult getRolesByStatus() throws Exception;

    //根据角色ID获取权限信息
    public ApiResponseResult getRolePerm(Long id) throws Exception;

    //设置权限
    public ApiResponseResult doRolePerm(Long roleId, String permIds) throws Exception;

    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;
}
