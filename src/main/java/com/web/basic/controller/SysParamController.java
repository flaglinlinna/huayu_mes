package com.web.basic.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.SysParam;
import com.web.basic.service.SysParamService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "系统参数信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysParam")
public class SysParamController extends WebController{

	private String module = "系统参数信息";

	 @Autowired
	 private SysParamService paramService;
	 
	 @ApiOperation(value = "系统参数基础信息表结构", notes = "系统参数基础信息表结构"+SysParam.TABLE_NAME)
	    @RequestMapping(value = "/getSysParam", method = RequestMethod.GET)
		@ResponseBody
	    public SysParam getSysParam(){
	        return new SysParam();
	    }
	 
	 
	 @ApiOperation(value = "系统参数信息列表页", notes = "系统参数信息列表页", hidden = true)
	    @RequestMapping(value = "/toSysParam")
	    public String toSysParam(){
	        return "/system/param/param";
	    }
	    @ApiOperation(value = "获取系统参数信息列表", notes = "获取系统参数信息列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "sysParam/getList";String methodName ="获取系统参数信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.ASC, "id");
	            ApiResponseResult result = paramService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取系统参数信息列表=getList:");
	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取系统参数信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取系统参数信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增系统参数信息", notes = "新增系统参数信息",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody SysParam param) {
	        String method = "sysParam/add";String methodName ="新增系统参数信息";
	        try{
	            ApiResponseResult result = paramService.add(param);
	            logger.debug("新增系统参数信息=add:");
	            getSysLogService().success(module,method, methodName, param.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("系统参数信息新增失败！", e);
	            getSysLogService().error(module,method, methodName, param.toString()+";"+e.toString());
	            return ApiResponseResult.failure("系统参数信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑系统参数信息", notes = "编辑系统参数信息",hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody SysParam param){
	        String method = "sysParam/edit";String methodName ="编辑系统参数信息";
	        try{
	            ApiResponseResult result = paramService.edit(param);
	            logger.debug("编辑系统参数信息=edit:");
	            getSysLogService().success(module,method, methodName, param.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑系统参数信息失败！", e);
	            getSysLogService().error(module,method, methodName,param.toString()+";"+ e.toString());
	            return ApiResponseResult.failure("编辑系统参数信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取系统参数信息", notes = "根据ID获取系统参数信息",hidden = true)
	    @RequestMapping(value = "/getSysParam", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getSysParam(@RequestBody Map<String, Object> params){
	        String method = "sysParam/getSysParam";String methodName ="根据ID获取系统参数信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = paramService.getSysParam(id);
	            logger.debug("根据ID获取系统参数信息=getSysParam:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取系统参数信息失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("获取系统参数信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除系统参数信息", notes = "删除系统参数信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "sysParam/delete";String methodName ="删除系统参数信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = paramService.delete(id);
	            logger.debug("删除系统参数信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除系统参数信息失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("删除系统参数信息失败！");
	        }
	    }

}
