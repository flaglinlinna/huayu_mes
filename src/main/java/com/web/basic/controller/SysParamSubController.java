package com.web.basic.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.SysParamSub;
import com.web.basic.service.SysParamSubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "系统参数信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysParamSub")
public class SysParamSubController extends WebController{

	 private String module = "系统子参数信息";

	 @Autowired
	 private SysParamSubService sysParamSubService;

	@ApiOperation(value = "系统子参数基础信息表结构", notes = "系统子参数基础信息表结构"+SysParamSub.TABLE_NAME)
	@RequestMapping(value = "/getSysParamSub", method = RequestMethod.GET)
	@ResponseBody
	public SysParamSub getSysParam(){
		return new SysParamSub();
	}
	 


	    @ApiOperation(value = "获取子系统参数信息列表", notes = "获取系统参数信息列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(long id) {
		//传入主表id
	        String method = "sysParam/getList";String methodName ="获取系统子参数信息列表";
	        try {
	            Sort sort = new Sort(Sort.Direction.ASC, "id");
	            ApiResponseResult result = sysParamSubService.getList(id, super.getPageRequest(sort));
	            logger.debug("获取系统子参数信息列表=getList:");
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取系统子参数信息列表失败！", e);
				getSysLogService().error(module,method, methodName,"主表id"+id==null?";":id+";"+e.toString());
	            return ApiResponseResult.failure("获取系统子参数信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增系统参数信息", notes = "新增系统参数信息",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody SysParamSub param) {
	        String method = "sysParam/add";String methodName ="新增系统参数信息";
	        try{
	            ApiResponseResult result = sysParamSubService.add(param);
	            logger.debug("新增系统子参数信息=add:");
	            getSysLogService().success(module,method, methodName, param.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("系统子参数信息新增失败！", e);
	            getSysLogService().error(module,method, methodName, param.toString()+";"+e.toString());
	            return ApiResponseResult.failure("系统子参数信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑系统参数信息", notes = "编辑系统参数信息",hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody SysParamSub param){
	        String method = "sysParam/edit";String methodName ="编辑系统参数信息";
	        try{
	            ApiResponseResult result = sysParamSubService.edit(param);
	            logger.debug("编辑系统子参数信息=edit:");
	            getSysLogService().success(module,method, methodName, param.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑系统子参数信息失败！", e);
	            getSysLogService().error(module,method, methodName,param.toString()+";"+ e.toString());
	            return ApiResponseResult.failure("编辑系统子参数信息失败！");
	        }
	    }

		
		@ApiOperation(value = "删除系统子参数信息", notes = "删除系统子参数信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "sysParam/delete";String methodName ="删除系统子参数信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = sysParamSubService.delete(id);
	            logger.debug("删除系统子参数信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除系统子参数信息失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("删除系统子参数信息失败！");
	        }
	    }

}
