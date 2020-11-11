package com.web.basic.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	private String module = "客户通用工艺维护模块";

	 @Autowired
	 private ClientProcessMapService clientProcessMapService;
	 
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
	            //Sort sort = new Sort(Sort.Direction.DESC, "id");
	        	 Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "createDate");
	        	 Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "process.procOrder");
	        	 List<Sort.Order> list = new ArrayList<>();
	        	 list.add(order1);
	        	 list.add(order2);
	        	 Sort sort = new Sort(list);
	            ApiResponseResult result = clientProcessMapService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取客户通用工艺维护列表=getList:");
	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取客户通用工艺维护列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取客户通用工艺维护列表失败！");
	        }
	    }
	    @ApiOperation(value = "获取工序列表", notes = "获取工序列表", hidden = true)
	    @RequestMapping(value = "/getProcList", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getProcList() {
	        String method = "base/client_proc/getProcList";String methodName ="获取工序列表";
	        try {
	            ApiResponseResult result = clientProcessMapService.getProcList();
	            logger.debug("获取工序列表=getProcList:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取工序列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取工序列表失败！");
	        }
	    }
	    
	    @ApiOperation(value = "新增工艺信息", notes = "新增工艺信息",hidden = true)
	    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult addItem(@RequestBody Map<String, Object> params) {   	
	        String method = "base/client_proc/addItem";String methodName ="新增工艺信息";
	        try{
	        	
//	        	long clientId = Long.parseLong(params.get("client").toString());
	        	String fdemoName = params.get("fdemoName").toString();
	        	String procIdList = params.get("proc").toString();
	            ApiResponseResult result = clientProcessMapService.addItem(procIdList,fdemoName);
	            logger.debug("新增工艺信息=addItem:");
	            getSysLogService().success(module,method, methodName, "模板名称:"+fdemoName+";工序id:"+procIdList);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("工艺信息新增失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("工艺信息新增失败！");
	        }
	    }
	    @ApiOperation(value = "获取客户通用工艺信息", notes = "获取客户通用工艺信息",hidden = true)
	    @RequestMapping(value = "/getClientItem", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getClientItem(@RequestBody Map<String, Object> params) {   	
	        String method = "base/client_proc/getClientItem";String methodName ="获取客户通用工艺信息";
	        try{	        	
	        	String  fdemoName = params.get("fdemoName").toString();
	            ApiResponseResult result = clientProcessMapService.getClientItem(fdemoName);
	            logger.debug("获取客户通用工艺信息=getClientItem:");
	            getSysLogService().success(module,method, methodName, "模板名称："+fdemoName);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("获取客户通用工艺失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取客户通用工艺失败！");
	        }
	    }
	    
	    @ApiOperation(value = "删除工序信息", notes = "删除工序信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/client_proc/delete";String methodName ="删除工序信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = clientProcessMapService.delete(id);
	            logger.debug("删除工序信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除工序信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("删除客户信息失败！");
	        }
	    }
	    
	    @ApiOperation(value = "设置过程属性", notes = "设置过程属性", hidden = true)
	    @RequestMapping(value = "/doJobAttr", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult doJobAttr(@RequestBody Map<String, Object> params) throws Exception{
	        String method = "base/client_proc/doJobAttr";String methodName ="设置过程属性";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	        	Integer jobAttr=Integer.parseInt(params.get("jobAttr").toString());
	            ApiResponseResult result = clientProcessMapService.doJobAttr(id, jobAttr);
	            logger.debug("设置过程属性=doJobAttr:");
	            getSysLogService().success(module,method, methodName, "id:"+id+";属性"+jobAttr);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("设置过程属性失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("设置过程属性失败！");
	        }
	    }
	    
	    @ApiOperation(value = "修改顺序", notes = "修改顺序", hidden = true)
	    @RequestMapping(value = "/doProcOrder", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult doProcOrder(@RequestBody Map<String, Object> params) throws Exception{
	        String method = "base/client_proc/doProcOrder";String methodName ="修改顺序";
	        try{
	        	Long id = Long.parseLong(params.get("id").toString()) ;
	        	String procOrder=params.get("procOrder").toString();
	            ApiResponseResult result = clientProcessMapService.doProcOrder(id, procOrder);
	            logger.debug("修改顺序=doProcOrder:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("修改顺序失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("修改顺序失败！");
	        }
	    }
}
