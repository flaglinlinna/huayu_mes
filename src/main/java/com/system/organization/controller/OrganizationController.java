package com.system.organization.controller;

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

import com.alibaba.fastjson.JSON;
import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.organization.entity.SysOrganization;
import com.system.organization.service.OrganizationService;
import com.system.permission.entity.SysPermission;
import com.system.user.entity.SysUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "组织管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysOrg")
public class OrganizationController extends WebController {
	
	private String module = "组织管理";

    @Autowired
    private OrganizationService organizationService;
    
    @ApiOperation(value = "组织表结构", notes = "组织表结构")
    @RequestMapping(value = "/getOrganization", method = RequestMethod.GET)
	@ResponseBody
    public SysOrganization getOrganization(){
        return new SysOrganization();
    }
 
    
    /**
	 * 组织列表
	 * @return ok/fail
	 */
	@RequestMapping(value = "/toOrgList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView permList() {
		String method = "/sysOrg/toOrgList";String methodName ="组织列表";
		//getSysLogService().debug(method,methodName);
		logger.debug(method);
		ModelAndView mav = new ModelAndView("/system/organization/orgList");
		try {
			List<SysOrganization> permList = organizationService.permList();
			logger.debug("组织列表查询=permList:" + permList);
			getSysLogService().success(module,method,methodName,permList);
			mav.addObject("permList", permList);
			mav.addObject("msg", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("组织列表查询异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
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
		String method = "/sysPermission/getPerm";String methodName ="获取权限";String params = "获取权限id:" + id;
		getSysLogService().debug(method,methodName);
		try {
			ApiResponseResult api = organizationService.getPermission(id);
//			getSysLogService().success(module,method,methodName,params);
			return api;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取权限异常！", e);
			getSysLogService().error(module,method,methodName,params+e.toString());
			return  ApiResponseResult.failure("获取权限操作失败，请联系管理员！");
		}
	}
	
	@ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
    public ApiResponseResult delete(@RequestParam(value = "id", required = false) Long id){
		String method = "/sysPermission/delete";String methodName ="删除组织列表记录";String params ="id:"+id;
        try{
        	ApiResponseResult api = organizationService.delete(id);
        	getSysLogService().success(module,method,methodName,params);
            return api;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            getSysLogService().error(module,method,methodName,params+";"+e.toString());
            return  ApiResponseResult.failure("删除组织列表记录操作失败，请联系管理员！");
        }
    }
	
	/**
	 * 添加组织列表记录
	 * @param type [0：编辑；1：新增子节点组织列表记录]
	 * @param permission
	 * @return ModelAndView ok/fail
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody public ApiResponseResult add(
			@RequestParam("type") int type, SysOrganization permission) {
		logger.debug("设置组织列表记录--区分type-" + type + "【0：编辑；1：新增子节点组织列表记录】，组织列表记录--permission-"
				+ permission);
		String method = "/sysPermission/add";String methodName ="添加组织列表记录";
		String param = "设置组织列表记录--区分type-" + type + "【0：编辑；1：新增子节点组织列表记录】，组织列表记录--permission-"
				+ JSON.toJSONString(permission);
		try {
			ApiResponseResult api = organizationService.savePerm(permission);
        	getSysLogService().success(module,method,methodName,permission.toString());
            return api;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设置组织列表记录异常！", e);
			getSysLogService().error(module,method,methodName,permission.toString()+";"+e.toString());
			return ApiResponseResult.failure("设置组织列表记录异常，请联系管理员");
		}
		//return "设置组织列表记录出错，请您稍后再试";
	}
	
	/**
	 * 根据用户id查询组织列表记录树数据
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping(value = "/getUserPerms", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getUserPerms() {
		String method = "/sysPermission/getUserPerms";String methodName ="查询用户的组织列表记录";
		logger.debug("根据用户id查询限树列表！");
		SysUser existUser= (SysUser) SecurityUtils.getSubject().getPrincipal();
		if(null==existUser){
			logger.debug("根据用户id查询限树列表！用户未登录");
			getSysLogService().error(module,method,methodName,"查询用户组织列表记录失败,用户未登录");
			return ApiResponseResult.failure("用户未登录");
		}
		try {
			return organizationService.getUserPerms(existUser.getId());
//            return sysPermissionService.getUserPerms(Long.parseLong(existUser.getFid()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据用户id查询组织列表记录树列表查询异常！", e);
			return ApiResponseResult.failure("根据用户id查询组织列表记录树列表查询异常");
		}
	}
	
}
