package com.web.basic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.system.organization.entity.SysOrganization;
import com.system.permission.entity.SysPermission;
import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.SysUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "系统管理模块的所有表")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/sys")
public class ASysModelController extends WebController{

	@ApiOperation(value = "用户基础信息表结构", notes = "用户基础信息表结构"+SysUser.TABLE_NAME)
    @RequestMapping(value = "/getSysUser", method = RequestMethod.GET)
	@ResponseBody
    public SysUser getSysUser(){
        return new SysUser();
    }
	
	@ApiOperation(value = "组织基础信息表结构", notes = "组织基础信息表结构"+SysOrganization.TABLE_NAME)
    @RequestMapping(value = "/getSysOrganization", method = RequestMethod.GET)
	@ResponseBody
    public SysOrganization getSysOrganization(){
        return new SysOrganization();
    }
	
	@ApiOperation(value = "角色基础信息表结构", notes = "角色基础信息表结构"+SysRole.TABLE_NAME)
    @RequestMapping(value = "/getSysRole", method = RequestMethod.GET)
	@ResponseBody
    public SysRole getSysRole(){
        return new SysRole();
    }
	
	@ApiOperation(value = "菜单基础信息表结构", notes = "菜单基础信息表结构"+SysPermission.TABLE_NAME)
    @RequestMapping(value = "/getSysPermission", method = RequestMethod.GET)
	@ResponseBody
    public SysPermission getSysPermission(){
        return new SysPermission();
    }
	
	@ApiOperation(value = "角色菜单关系表结构", notes = "角色菜单关系表结构"+RolePermissionMap.TABLE_NAME)
    @RequestMapping(value = "/getRolePermissionMap", method = RequestMethod.GET)
	@ResponseBody
    public RolePermissionMap getRolePermissionMap(){
        return new RolePermissionMap();
    }

}
