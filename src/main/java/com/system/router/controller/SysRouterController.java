package com.system.router.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.data.ApiResponseResult;
import com.system.role.entity.SysRole;
import com.system.role.service.SysRoleService;
import com.system.router.entity.SysRouter;
import com.system.router.service.SysRouterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "角色管理模块")
@CrossOrigin
@ControllerAdvice
@RestController
@RequestMapping(value = "/sysRouter")
public class SysRouterController {

    @Autowired
    private SysRouterService sysRouterService;
    

    @ApiOperation(value = "新增资源", notes = "新增资源")
    @PostMapping("/add")
    public ApiResponseResult add(@RequestBody(required=false) SysRouter sysRouter){
        try{
            return sysRouterService.add(sysRouter);
        }catch(Exception e){
            return ApiResponseResult.failure("角色新增失败！");
        }
    }

    @ApiOperation(value = "新增(编辑)资源", notes = "新增(编辑)资源")
    @RequestMapping(value = "/addRouter", method = RequestMethod.POST)
    public ApiResponseResult addRouter(SysRouter sysRouter){
        try{
            return sysRouterService.add(sysRouter);
        }catch(Exception e){
            return ApiResponseResult.failure("角色新增失败！");
        }
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @PostMapping("/delete")
    public ApiResponseResult delete(@RequestParam(value = "id", required = false) Long id){
        try{
            return sysRouterService.delete(id);
        }catch(Exception e){
            return ApiResponseResult.failure("删除角色失败！");
        }
    }

    @ApiOperation(value = "获取树形菜单列表", notes = "获取树形菜单列表")
    @RequestMapping(value = "/getTreeList", method = RequestMethod.GET)
    public ApiResponseResult getTreeList() {
        try {
            return sysRouterService.getTreeList();
        } catch (Exception e) {
            return ApiResponseResult.failure("获取树形菜单列表失败！");
        }
    }

    @ApiOperation(value = "根据角色编码获取配置资源", notes = "根据角色编码获取配置资源")
    @RequestMapping(value = "/getRouterTree", method = RequestMethod.GET)
    public ApiResponseResult getRouterTree(@RequestParam(value = "roleCode", required = false) String roleCode) {
        try {
            return sysRouterService.getRouterTree(roleCode);
        } catch (Exception e) {
        	System.out.println(e.toString());
            return ApiResponseResult.failure("获取配置资源失败！");
        }
    }
    
    
    @ApiOperation(value = "获取树形菜单列表", notes = "获取树形菜单列表")
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public ApiResponseResult getlist() {
        try {
            return sysRouterService.getlist("","");
        } catch (Exception e) {
            return ApiResponseResult.failure("获取树形菜单列表失败！");
        }
    }
    
}
