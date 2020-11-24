package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.ReworkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "在线返工模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/rework")
public class ReworkController extends WebController {

	private String module = "在线返工信息";
	 @Autowired
	 private ReworkService reworkService;
	 
	 @ApiOperation(value = "在线返工页", notes = "在线返工页", hidden = true)
	    @RequestMapping(value = "/toRework")
	    public String toRework(){
	        return "/web/produce/rework/rework";
	    }
	 
	  @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
	    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getTaskNo(String keyword) {
	        String method = "produce/rework/getTaskNo";String methodName ="获取指令单信息";
	        try {
	            ApiResponseResult result = reworkService.getTaskNo(keyword);
	            logger.debug("获取指令单信息=getTaskNo:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取指令单信息失败！", e);
	             getSysLogService().error(module,method, methodName, e.toString());
	             return ApiResponseResult.failure("获取指令单信息失败！");
	        }
	    }
	  
	  @ApiOperation(value="获取返工指令单信息", notes="获取返工指令单信息", hidden = true)
	    @RequestMapping(value = "/getReworkTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getReworkTaskNo(String keyword) {
	        String method = "produce/rework/getReworkTaskNo";String methodName ="获取返工指令单信息";
	        try {
	            ApiResponseResult result = reworkService.getReworkTaskNo(keyword);
	            logger.debug("获取返工指令单信息=getReworkTaskNo:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取返工指令单信息失败！", e);
	             getSysLogService().error(module,method, methodName, e.toString());
	             return ApiResponseResult.failure("获取返工指令单信息失败！");
	        }
	    }
	  
	  @ApiOperation(value="在线返工", notes="在线返工", hidden = true)
	    @RequestMapping(value = "/subCode", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult subCode(@RequestBody Map<String, Object> params) {
	        String method = "produce/rework/subCode";String methodName ="在线返工";
		  String taskNo = params.get("taskNo").toString();
		  String type = params.get("type") == null?"":params.get("type").toString();
		  String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
		  String memo = params.get("memo") == null?"":params.get("memo").toString();
		  String param = "制令单号:"+barcode +";条码:"+barcode +";类型："+ type;
	        try {
	            ApiResponseResult result = reworkService.subCode(taskNo,type,barcode,memo);
	            logger.debug("在线返工=subCode:");
	            getSysLogService().success(module,method, methodName, param);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("在线返工失败！", e);
	             getSysLogService().error(module,method, methodName, param+";"+e.toString());
	             return ApiResponseResult.failure("在线返工失败！");
	        }
	    }
	  
	  @ApiOperation(value="查询返工", notes="查询返工", hidden = true)
	    @RequestMapping(value = "/search", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult search(@RequestBody Map<String, Object> params) {
	        String method = "produce/rework/search";String methodName ="查询返工";
	        try {
	        	String startTime = params.get("startTime") == null?"":params.get("startTime").toString();
	        	String endTime = params.get("endTime") == null?"":params.get("endTime").toString();
	        	String taskNo = params.get("taskNo") == null?"":params.get("taskNo").toString();
	        	String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
	        	Sort sort =  Sort.unsorted();
	            ApiResponseResult result = reworkService.search(startTime,endTime,taskNo,barcode,super.getPageRequest(sort));
	            logger.debug("查询返工=search:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("查询返工失败！", e);
	             getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	             return ApiResponseResult.failure("查询返工失败！");
	        }
	    }
}
