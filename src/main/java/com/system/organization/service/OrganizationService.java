package com.system.organization.service;

import java.util.List;

import com.app.base.data.ApiResponseResult;
import com.system.organization.entity.SysOrganization;


/**
 * 菜单基础信息表
 */
public interface OrganizationService {
	
	List<SysOrganization> permList();
	
	public ApiResponseResult delete(Long id) throws Exception;
	
	public ApiResponseResult getPermission(Long id) throws Exception;
	
	public ApiResponseResult savePerm(SysOrganization permission) throws Exception;
	
	public ApiResponseResult getUserPerms(Long id) throws Exception;
}
