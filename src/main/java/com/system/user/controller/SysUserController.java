package com.system.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.user.entity.SysUser;
import com.system.user.service.SysUserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Api(description = "用户管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController extends WebController{
	
	private String module = "用户管理";

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "新增(编辑)用户", notes = "新增(编辑)用户")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(SysUser sysUser){
        String method = "/sysUser/add";String methodName ="新增(编辑)用户";
        try{
            ApiResponseResult result = sysUserService.add(sysUser);
            logger.debug("新增(编辑)用户=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("新增(编辑)用户失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("新增(编辑)用户失败！");
        }
    }
    
    @ApiOperation(value = "编辑用户（用于当前用户修改信息）", notes = "编辑用户（用于当前用户修改信息）")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(SysUser sysUser){
        String method = "/sysUser/edit";String methodName ="编辑用户";
        try{
            ApiResponseResult result = sysUserService.edit(sysUser);
            logger.debug("编辑用户=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑用户失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑用户失败！");
        }
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestParam(value = "id", required = false) Long id){
        String method = "/sysUser/delete";String methodName ="删除用户";
        try{
            ApiResponseResult result = sysUserService.delete(id);
            logger.debug("删除用户=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除用户失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除用户失败！");
        }
    }

    @RequestMapping(value = "/toUserInfo")
    @ResponseBody
    public ModelAndView toUserInfo() {
        ModelAndView mav = new ModelAndView("/system/user/info");
        try{
            ApiResponseResult result = sysUserService.getUserInfo();
            mav.addObject("userInfo", result.getData());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取当前用户信息异常！", e);
        }
        return mav;
    }

    @RequestMapping(value = "/toPassword")
    public String toPassword(){
        return "/system/user/password";
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/doChangePassword", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doChangePassword(@RequestParam(required=false) String oldPassword,
                                            @RequestParam(required=false) String password,
                                            @RequestParam(required = false) String rePassword){
        String method = "/sysUser/doChangePassword";String methodName ="修改密码";
        try{
            ApiResponseResult result = sysUserService.changePassword(oldPassword, password, rePassword);
            logger.debug("修改密码=doChangePassword:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改密码失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("修改密码失败！");
        }
    }

    @ApiOperation(value = "修改密码（管理员修改密码使用）", notes = "修改密码（管理员修改密码使用）")
    @RequestMapping(value = "/doSetPassword", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doSetPassword(@RequestParam(required=false) Long id,
                                            @RequestParam(required=false) String password,
                                            @RequestParam(required = false) String rePassword){
        String method = "/sysUser/doSetPassword";String methodName ="修改密码";
        try{
            ApiResponseResult result = sysUserService.resetPassword(id, password, rePassword);
            logger.debug("修改密码=doSetPassword:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改密码失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("修改密码失败！");
        }
    }
    
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "编码", dataType = "Long", paramType = "query", defaultValue = "")
    })
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public ApiResponseResult getUserInfo(@RequestParam(value = "token") String username ) {
        try {
            return sysUserService.getUserInfo(username);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            return ApiResponseResult.failure("用户注册失败！");
        }
    }

   /* @RequestMapping(value = "/toUserList")
    public String toUserList(){
        return "/system/user/userList";
    }*/
	@RequestMapping(value = "/toUserList", method = RequestMethod.GET)
	public ModelAndView toUserList() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("Roles", sysUserService.getListRole().getData());
		mav.setViewName("/system/user/userList");// 返回路径
		return mav;
	}
   
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "userCode", required = false) String userCode,
                                     @RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "mobile", required = false) String mobile,
                                     @RequestParam(value = "status", required = false) Integer status) {
        String method = "/sysUser/getList";String methodName ="获取用户列表";
        try {
        	Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = sysUserService.getList(keyword, userCode, userName, mobile, status, super.getPageRequest(sort));
            logger.debug("获取用户列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取用户列表失败！");
        }
    }


    @ApiOperation(value = "查询角色下用户列表", notes = "查询角色下用户列表")
    @RequestMapping(value = "/getListByRoleId", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getListByRoleId(Long roleId) {
        String method = "/sysUser/getListByRoleId";String methodName ="查询角色下用户列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = sysUserService.getListByRoleId(roleId, super.getPageRequest(sort));
            logger.debug("查询角色下用户列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询角色下用户列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("查询角色下用户列表失败！");
        }
    }

    @ApiOperation(value = "获取用户和角色信息", notes = "获取用户和角色信息")
    @RequestMapping(value = "/getUserAndRoles", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getUserAndRoles(Long id){
        String method = "/sysUser/getUserAndRoles";String methodName ="获取用户和角色信息";
        try{
            ApiResponseResult result = sysUserService.getUserAndRoles(id);
            logger.debug("获取用户和角色信息=getUserAndRoles:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取用户和角色信息失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取用户和角色信息失败！");
        }
    }

    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用")
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(Long id, Integer status) throws Exception{
        String method = "/sysUser/doStatus";String methodName ="设置正常/禁用";
        try{
            ApiResponseResult result = sysUserService.doStatus(id, status);
            logger.debug("设置正常/禁用=doJob:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置正常/禁用失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("设置正常/禁用失败！");
        }
    }
}
