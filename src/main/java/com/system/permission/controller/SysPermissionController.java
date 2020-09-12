package com.system.permission.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.permission.entity.SysPermission;
import com.system.permission.service.SysPermissionService;
import com.system.user.entity.SysUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "菜单管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysPermission")
public class SysPermissionController extends WebController {

    @Autowired
    private SysPermissionService sysPermissionService;
    
    /**
	 * 权限列表
	 * @return ok/fail
	 */
	@RequestMapping(value = "/toPermList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView permList() {
		String method = "/sysPermission/permList";String methodName ="权限列表";
		//getSysLogService().debug(method,methodName);
		logger.debug("/sysPermission/permList");
		ModelAndView mav = new ModelAndView("/system/permission/permList");
		try {
			List<SysPermission> permList = sysPermissionService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			getSysLogService().success(method,methodName,permList);
			mav.addObject("permList", permList);
			mav.addObject("msg", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("权限查询异常！", e);
			getSysLogService().error(method,methodName,e.toString());
		}
		return mav;
	}
	
	/**
	 * 获取权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getPerm", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getPerm(
			@RequestParam("id") Long id) {
		logger.debug("获取权限--id-" + id);
		String method = "/sysPermission/getPerm";String methodName ="获取权限";
		getSysLogService().debug(method,methodName);
		try {
			ApiResponseResult api = sysPermissionService.getPermission(id);
			getSysLogService().success(method,methodName,"权限实体");
			return api;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取权限异常！", e);
			getSysLogService().error(method,methodName,e.toString());
			return  ApiResponseResult.failure("获取权限操作失败，请联系管理员！");
		}
	}
	
	@ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
    public ApiResponseResult delete(@RequestParam(value = "id", required = false) Long id){
		String method = "/sysPermission/delete";String methodName ="删除权限";
        try{
        	ApiResponseResult api = sysPermissionService.delete(id);
        	getSysLogService().success(method,methodName,id);
            return api;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            getSysLogService().error(method,methodName,e.toString());
            return  ApiResponseResult.failure("删除权限操作失败，请联系管理员！");
        }
    }
	
	/**
	 * 添加权限
	 * @param type [0：编辑；1：新增子节点权限]
	 * @param permission
	 * @return ModelAndView ok/fail
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody public ApiResponseResult add(
			@RequestParam("type") int type, SysPermission permission) {
		logger.debug("设置权限--区分type-" + type + "【0：编辑；1：新增子节点权限】，权限--permission-"
				+ permission);
		String method = "/sysPermission/add";String methodName ="添加权限";
		try {
			ApiResponseResult api = sysPermissionService.savePerm(permission);
        	getSysLogService().success(method,methodName,"权限实体");
            return api;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设置权限异常！", e);
			getSysLogService().error(method,methodName,e.toString());
			return ApiResponseResult.failure("设置权限异常，请联系管理员");
		}
		//return "设置权限出错，请您稍后再试";
	}
	
	/**
	 * 根据用户id查询权限树数据
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getUserPerms", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getUserPerms() {
		String method = "/sysPermission/getUserPerms";String methodName ="查询用户的权限";
		logger.debug("根据用户id查询限树列表！");
		SysUser existUser= (SysUser) SecurityUtils.getSubject().getPrincipal();
		if(null==existUser){
			logger.debug("根据用户id查询限树列表！用户未登录");
			getSysLogService().error(method,methodName,"查询用户权限失败,用户未登录");
			return ApiResponseResult.failure("用户未登录");
		}
		try {
			return sysPermissionService.getUserPerms(existUser.getId());
//            return sysPermissionService.getUserPerms(Long.parseLong(existUser.getFid()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据用户id查询权限树列表查询异常！", e);
			return ApiResponseResult.failure("根据用户id查询权限树列表查询异常");
		}
	}
	
}
