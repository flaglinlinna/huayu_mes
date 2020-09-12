package com.system.permission.service;

import java.util.List;

import com.app.base.data.ApiResponseResult;
import com.system.permission.entity.SysPermission;


/**
 * 菜单基础信息表
 */
public interface SysPermissionService {
	
	List<SysPermission> permList();
	
	public ApiResponseResult delete(Long id) throws Exception;
	
	public ApiResponseResult getPermission(Long id) throws Exception;
	
	public ApiResponseResult savePerm(SysPermission permission) throws Exception;
	
	public ApiResponseResult getUserPerms(Long id) throws Exception;
}
