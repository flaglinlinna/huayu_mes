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
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.Hours;
import com.web.basic.service.HoursService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工时信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/hours")
public class HoursController extends WebController{

	private String module = "工时信息";

	 @Autowired
	 private HoursService hoursService;
	 
	 @ApiOperation(value = "工时信息表结构", notes = "工时信息表结构"+Hours.TABLE_NAME)
	    @RequestMapping(value = "/getHours", method = RequestMethod.GET)
		@ResponseBody
	    public Hours getHours(){
	        return new Hours();
	    }
	 
	 @ApiOperation(value = "工时信息列表页", notes = "工时信息列表页", hidden = true)
	    @RequestMapping(value = "/toHours")
	    public String toHours(){
	        return "/web/basic/hours";
	    }
	    @ApiOperation(value = "获取工时信息列表", notes = "获取工时信息列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/hours/getList";String methodName ="获取工时信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = hoursService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取工时信息列表=getList:");
	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取工时信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取工时信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增工时信息", notes = "新增工时信息", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Hours hours) {
	        String method = "base/hours/add";String methodName ="新增工时信息";
	        try{
	            ApiResponseResult result = hoursService.add(hours);
	            logger.debug("新增工时信息=add:");
	            getSysLogService().success(module,method, methodName, hours.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("工时信息新增失败！", e);
	            getSysLogService().error(module,method, methodName,hours.toString()+";"+ e.toString());
	            return ApiResponseResult.failure("工时信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑工时信息", notes = "编辑工时信息", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Hours hours){
	        String method = "base/hours/edit";String methodName ="编辑工时信息";
	        try{
	            ApiResponseResult result = hoursService.edit(hours);
	            logger.debug("编辑工时信息=edit:");
	            getSysLogService().success(module,method, methodName, hours.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑工时信息失败！", e);
	            getSysLogService().error(module,method, methodName, hours.toString()+';'+e.toString());
	            return ApiResponseResult.failure("编辑工时信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取工时信息", notes = "根据ID获取工时信息", hidden = true)
	    @RequestMapping(value = "/getHours", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getHours(@RequestBody Map<String, Object> params){
	        String method = "base/hours/getHours";String methodName ="根据ID获取工时信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = hoursService.getHours(id);
	            logger.debug("根据ID获取工时信息=getHours:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取工时信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取工时信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除工时信息", notes = "删除工时信息", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/hours/delete";String methodName ="删除工时信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = hoursService.delete(id);
	            logger.debug("删除工时信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除工时信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("删除工时信息失败！");
	        }
	    }
}
