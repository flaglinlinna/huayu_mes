package com.system.role.controller;

import java.util.Date;
import java.util.List;

import com.app.base.control.WebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.app.base.data.ApiResponseResult;
import com.system.role.entity.SysRole;
import com.system.role.service.SysRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

@Api(description = "角色管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysRole")
public class SysRoleController extends WebController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation(value = "新增角色", notes = "新增角色")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(SysRole sysRole){
        String method = "/sysRole/add";String methodName ="新增角色";
        try{
            ApiResponseResult result = sysRoleService.add(sysRole);
            logger.debug("新增角色=add:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("角色新增失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("角色新增失败！");
        }
    }
    
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(SysRole sysRole){
        String method = "/sysRole/edit";String methodName ="编辑角色";
        try{
            ApiResponseResult result = sysRoleService.edit(sysRole);
            logger.debug("编辑角色=edit:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑角色失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("编辑角色失败！");
        }
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestParam(value = "id", required = false) Long id){
        String method = "/sysRole/delete";String methodName ="删除角色";
        try{
            ApiResponseResult result = sysRoleService.delete(id);
            logger.debug("删除角色=delete:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除角色失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("删除角色失败！");
        }
    }

    @ApiOperation(value = "角色列表页", notes = "角色列表页", hidden = true)
    @RequestMapping(value = "/toRoleList")
    public String toUserList(){
        return "/system/role/roleList";
    }

    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword, String bsCode, String bsName, Date createdTimeStart, Date createdTimeEnd, Integer bsStatus) {
        String method = "/sysRole/getList";String methodName ="获取角色列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = sysRoleService.getList(keyword, bsCode,bsName, createdTimeStart, createdTimeEnd, bsStatus, super.getPageRequest(sort));
            logger.debug("获取角色列表=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取角色列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取角色列表失败！");
        }
    }
    @ApiOperation(value = "获取角色配置列表", notes = "获取角色配置列表")
    @RequestMapping(value = "/getCheckedRoles", method = RequestMethod.GET)
    public ApiResponseResult getCheckedRoles(@RequestParam(value = "userId") String userId) {
        try {
            return sysRoleService.getCheckedRoles(Long.parseLong(userId));
        } catch (Exception e) {
            return ApiResponseResult.failure("获取角色列表配置失败！");
        }
    }
    
    @ApiOperation(value = "获取角色配置列表", notes = "获取角色配置列表")
    @RequestMapping(value = "/saveUserRoles", method = RequestMethod.GET)
    public ApiResponseResult saveUserRoles(@RequestParam(value = "userId") String userId,@RequestParam(value = "roles") String roles) {
        try {
            return sysRoleService.saveUserRoles(Long.parseLong(userId),roles);
        } catch (Exception e) {
            return ApiResponseResult.failure("获取角色列表配置失败！");
        }
    }
    
    @ApiOperation(value = "新增资源", notes = "新增资源")
    @RequestMapping(value = "/addRouter", method = RequestMethod.GET)
    public ApiResponseResult addRouter(@RequestParam(value = "roleCode") String roleCode,@RequestParam(value = "roles") String roles){
        try{
        	return sysRoleService.addRouter(roleCode,roles);
        }catch(Exception e){
            return ApiResponseResult.failure("角色资源失败！");
        }
    }

    @ApiOperation(value = "获取配置资源", notes = "获取配置资源")
    @RequestMapping(value = "/getRouter", method = RequestMethod.GET)
    public ApiResponseResult getRouter(@RequestParam(value = "roleCode", required = false) String roleCode) {
        try {
            return sysRoleService.getRouter(roleCode);
        } catch (Exception e) {
            return ApiResponseResult.failure("获取配置资源失败！");
        }
    }

    @ApiOperation(value = "获取当前角色的操作权限", notes = "获取当前角色的操作权限")
    @RequestMapping(value = "/getPermission", method = RequestMethod.GET)
    public ApiResponseResult getPermission(){
        try{
            return sysRoleService.getPermission();
        } catch (Exception e) {
            return ApiResponseResult.failure("获取当前角色的操作权限失败！");
        }
    }

    @ApiOperation(value = "根据ID获取角色", notes = "根据ID获取角色")
    @RequestMapping(value = "/getRole", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getRole(Long id){
        String method = "/sysRole/getRole";String methodName ="根据ID获取角色";
        try{
            ApiResponseResult result = sysRoleService.getRole(id);
            logger.debug("根据ID获取角色=getRole:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取角色失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取角色失败！");
        }
    }

    @ApiOperation(value = "获取所有角色", notes = "获取所有角色")
    @RequestMapping(value = "/getRoles", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getRoles(){
        String method = "/sysRole/getRoles";String methodName ="获取所有角色";
        try{
            ApiResponseResult result = sysRoleService.getRoles();
            logger.debug("获取所有角色=getRoles:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取所有角色失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取所有角色失败！");
        }
    }

    @ApiOperation(value = "根据角色ID获取权限信息", notes = "根据角色ID获取权限信息")
    @RequestMapping(value = "/getRolePerm", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getRolePerm(Long id){
        String method = "/sysRole/getRolePerm";String methodName ="根据角色ID获取权限信息";
        try{
            ApiResponseResult result = sysRoleService.getRolePerm(id);
            logger.debug("根据角色ID获取权限信息=getRolePerm:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取权限信息失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取权限信息失败！");
        }
    }

    @ApiOperation(value = "设置权限", notes = "设置权限")
    @RequestMapping(value = "/doRolePerm", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doRolePerm(Long roleId, String permIds){
        String method = "/sysRole/doRolePerm";String methodName ="设置权限";
        try{
            ApiResponseResult result = sysRoleService.doRolePerm(roleId, permIds);
            logger.debug("设置权限=doRolePerm:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置权限失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("设置权限失败！");
        }
    }

    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用")
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        String method = "/sysRole/doStatus";String methodName ="设置正常/禁用";
        try{
            ApiResponseResult result = sysRoleService.doStatus(id, bsStatus);
            logger.debug("设置正常/禁用=doJob:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置正常/禁用失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("设置正常/禁用失败！");
        }
    }
}
