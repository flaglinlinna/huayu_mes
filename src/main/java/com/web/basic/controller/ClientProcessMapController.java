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
import com.web.basic.entity.ClientProcessMap;
import com.web.basic.entity.Mtrial;
import com.web.basic.service.ClientProcessMapService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "客户通用工艺维护模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/client_proc")
public class ClientProcessMapController extends WebController{

	 @Autowired
	 private ClientProcessMapService clientService;
	 
	 @ApiOperation(value = "客户通用工艺维护列表结构", notes = "客户通用工艺维护列表结构"+ClientProcessMap.TABLE_NAME)
	    @RequestMapping(value = "/getClientProcessMap", method = RequestMethod.GET)
		@ResponseBody
	    public ClientProcessMap getClientProcessMap(){
	        return new ClientProcessMap();
	    }
	 
	 
	 @ApiOperation(value = "客户通用工艺维护列表页", notes = "客户通用工艺维护列表页", hidden = true)
	    @RequestMapping(value = "/toClientProcess")
	    public String toClientProcess(){
	        return "/web/basic/client_proc";
	    }
	    @ApiOperation(value = "获取客户通用工艺维护列表", notes = "获取客户通用工艺维护列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/client_proc/getList";String methodName ="获取客户通用工艺维护列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = clientService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取客户通用工艺维护列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取客户通用工艺维护列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取客户通用工艺维护列表失败！");
	        }
	    }
	    @ApiOperation(value = "获取工序列表", notes = "获取工序列表", hidden = true)
	    @RequestMapping(value = "/getProcList", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getProcList() {
	        String method = "base/defdetail/getProcList";String methodName ="获取工序列表";
	        try {
	            ApiResponseResult result = clientService.getProcList();
	            logger.debug("获取工序列表=getProcList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取工序列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取工序列表失败！");
	        }
	    }
	    
	    @ApiOperation(value = "新增工艺信息", notes = "新增工艺信息",hidden = true)
	    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult addItem(@RequestBody Map<String, Object> params) {   	
	        String method = "base/client/addItem";String methodName ="新增工艺信息";
	        try{
	        	
	        	long clientId = Long.parseLong(params.get("client").toString());
	        	String procIdList = params.get("proc").toString();
	            ApiResponseResult result = clientService.addItem(procIdList,clientId);
	            logger.debug("新增工艺信息=addItem:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("工艺信息新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("工艺信息新增失败！");
	        }
	    }
}
