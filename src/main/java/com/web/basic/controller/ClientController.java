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
import com.web.basic.entity.Client;
import com.web.basic.service.ClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "不良内容信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/client")
public class ClientController extends WebController{

	 @Autowired
	 private ClientService clientService;
	 
	 @ApiOperation(value = "不良内容列表页", notes = "不良内容列表页", hidden = true)
	    @RequestMapping(value = "/toClient")
	    public String toClient(){
	        return "/web/basic/client";
	    }
	    @ApiOperation(value = "获取不良内容列表", notes = "获取不良内容列表")
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/client/getList";String methodName ="获取不良内容列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = clientService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取不良内容列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取不良内容列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取不良内容列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增不良内容", notes = "新增不良内容")
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Client client) {
	        String method = "base/client/add";String methodName ="新增不良内容";
	        try{
	            ApiResponseResult result = clientService.add(client);
	            logger.debug("新增不良内容=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("不良内容新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("不良内容新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑不良内容", notes = "编辑不良内容")
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Client client){
	        String method = "base/client/edit";String methodName ="编辑不良内容";
	        try{
	            ApiResponseResult result = clientService.edit(client);
	            logger.debug("编辑不良内容=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑不良内容失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑不良内容失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取不良内容", notes = "根据ID获取不良内容")
	    @RequestMapping(value = "/getClient", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getClient(@RequestBody Map<String, Object> params){
	        String method = "base/client/getClient";String methodName ="根据ID获取不良内容";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = clientService.getClient(id);
	            logger.debug("根据ID获取不良内容=getClient:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取不良内容失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取不良内容失败！");
	        }
	    }
		
		@ApiOperation(value = "删除不良内容", notes = "删除不良内容")
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/client/delete";String methodName ="删除不良内容";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = clientService.delete(id);
	            logger.debug("删除不良内容=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除不良内容失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除不良内容失败！");
	        }
	    }

}
