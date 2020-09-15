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
import com.web.basic.entity.WoHours;
import com.web.basic.service.WoHoursService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工时信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/hours")
public class WoHoursController extends WebController{

	 @Autowired
	 private WoHoursService woHoursService;
	 
	 @ApiOperation(value = "hours列表页", notes = "工时信息列表页", hidden = true)
	    @RequestMapping(value = "/toWoHours")
	    public String toWoHours(){
	        return "/web/basic/hours";
	    }
	    @ApiOperation(value = "获取工时信息列表", notes = "获取工时信息列表")
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/hours/getList";String methodName ="获取工时信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = woHoursService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取工时信息列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取工时信息列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取工时信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增工时信息", notes = "新增工时信息")
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody WoHours hours) {
	        String method = "base/hours/add";String methodName ="新增工时信息";
	        try{
	            ApiResponseResult result = woHoursService.add(hours);
	            logger.debug("新增工时信息=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("工时信息新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("工时信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑工时信息", notes = "编辑工时信息")
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody WoHours hours){
	        String method = "base/hours/edit";String methodName ="编辑工时信息";
	        try{
	            ApiResponseResult result = woHoursService.edit(hours);
	            logger.debug("编辑工时信息=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑工时信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑工时信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取工时信息", notes = "根据ID获取工时信息")
	    @RequestMapping(value = "/getWoHours", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getWoHours(@RequestBody Map<String, Object> params){
	        String method = "base/hours/getWoHours";String methodName ="根据ID获取工时信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = woHoursService.getWoHours(id);
	            logger.debug("根据ID获取工时信息=getWoHours:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取工时信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取工时信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除工时信息", notes = "删除工时信息")
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/hours/delete";String methodName ="删除工时信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = woHoursService.delete(id);
	            logger.debug("删除工时信息=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除工时信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除工时信息失败！");
	        }
	    }
}
