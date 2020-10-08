package com.web.produce.controller;

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
import com.web.produce.entity.Clear;
import com.web.produce.entity.DevClock;
import com.web.produce.service.ClearService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "指纹清除记录模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "produce/clear")
public class ClearController extends WebController{

	 @Autowired
	 private ClearService clearService;
	 
	 @ApiOperation(value = "指纹清除记录表结构", notes = "指纹清除记录表结构"+Clear.TABLE_NAME)
	    @RequestMapping(value = "/getClear", method = RequestMethod.GET)
		@ResponseBody
	    public Clear getClear(){
	        return new Clear();
	    }
	  
	 @ApiOperation(value = "指纹清除记录列表页", notes = "指纹清除记录列表页", hidden = true)
	    @RequestMapping(value = "/toClear")
	    public String toClear(){
	        return "/web/produce/dev_clock/clear";
	    }
	 
	    @ApiOperation(value = "获取指纹清除记录列表", notes = "获取指纹清除记录列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "produce/clear/getList";String methodName ="获取指纹清除记录列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = clearService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取指纹清除记录列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取指纹清除记录列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取指纹清除记录列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增清除记录", notes = "新增清除记录",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Map<String, Object> params) {
	        String method = "produce/clear/add";String methodName ="新增清除记录";
	        try{
	        	String devList = params.get("devList").toString();
	        	String empList = params.get("empList").toString();
	            ApiResponseResult result = clearService.add(devList,empList);
	            logger.debug("新增清除记录=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("清除记录新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("清除记录新增失败！");
	        }
	    }
	    @ApiOperation(value = "根据ID获取清除记录", notes = "根据ID获取清除记录",hidden = true)
	    @RequestMapping(value = "/getClear", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getClear(@RequestBody Map<String, Object> params){
	        String method = "produce/clear/getClear";String methodName ="根据ID获取清除记录";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = clearService.getClear(id);
	            logger.debug("根据ID获取清除记录=getClear:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取清除记录失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取清除记录失败！");
	        }
	    }
		
		@ApiOperation(value = "删除清除记录", notes = "删除清除记录",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "produce/clear/delete";String methodName ="删除清除记录";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = clearService.delete(id);
	            logger.debug("删除清除记录=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除清除记录失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除清除记录失败！");
	        }
	    }
		
		@ApiOperation(value = "获取人员信息列表", notes = "获取人员信息列表", hidden = true)
	    @RequestMapping(value = "/getEmp", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getEmp(String empKeyword) {
	        String method = "produce/clear/getEmp";String methodName ="获取人员信息列表";
	        try {
	        	Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = clearService.getEmp(empKeyword, super.getPageRequest(sort));
	            logger.debug("获取人员信息列表=getEmp:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取人员信息列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取人员信息列表失败！");
	        }
	    }
		@ApiOperation(value = "获取卡机信息列表", notes = "获取卡机信息列表", hidden = true)
	    @RequestMapping(value = "/getDev", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getDev(String devKeyword) {
	        String method = "produce/clear/getDev";String methodName ="获取卡机信息列表";
	        try {
	        	Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = clearService.getDev(devKeyword, super.getPageRequest(sort));
	            logger.debug("获取卡机信息列表=getDev:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取卡机信息列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取卡机信息列表失败！");
	        }
	    }
}
