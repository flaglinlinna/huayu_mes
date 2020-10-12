package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.OnlineReworkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "在线返工模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/rework")
public class OnlineReworkController extends WebController {

	 @Autowired
	 private OnlineReworkService onlineReworkService;
	 
	 @ApiOperation(value = "在线返工页", notes = "在线返工页", hidden = true)
	    @RequestMapping(value = "/toOnlineRework")
	    public String toOnlineRework(){
	        return "/web/produce/rework";
	    }
	 
	  @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
	    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getTaskNo(String keyword) {
	        String method = "produce/rework/getTaskNo";String methodName ="获取指令单信息";
	        try {
	            ApiResponseResult result = onlineReworkService.getTaskNo(keyword);
	            logger.debug("获取指令单信息=getTaskNo:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取指令单信息失败！", e);
	             getSysLogService().error(method, methodName, e.toString());
	             return ApiResponseResult.failure("获取指令单信息失败！");
	        }
	    }
	  
	  @ApiOperation(value="获取返工指令单信息", notes="获取返工指令单信息", hidden = true)
	    @RequestMapping(value = "/getReworkTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getReworkTaskNo(String keyword) {
	        String method = "produce/rework/getReworkTaskNo";String methodName ="获取返工指令单信息";
	        try {
	            ApiResponseResult result = onlineReworkService.getReworkTaskNo(keyword);
	            logger.debug("获取返工指令单信息=getReworkTaskNo:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取返工指令单信息失败！", e);
	             getSysLogService().error(method, methodName, e.toString());
	             return ApiResponseResult.failure("获取返工指令单信息失败！");
	        }
	    }
	  
	  @ApiOperation(value="在线返工", notes="在线返工", hidden = true)
	    @RequestMapping(value = "/subCode", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult subCode(@RequestBody Map<String, Object> params) {
	        String method = "produce/rework/subCode";String methodName ="在线返工";
	        try {
	        	String taskNo = params.get("taskNo").toString();
	        	String type = params.get("type") == null?"":params.get("type").toString();
	        	String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
	        	String memo = params.get("memo") == null?"":params.get("memo").toString();
	            ApiResponseResult result = onlineReworkService.subCode(taskNo,type,barcode,memo);
	            logger.debug("在线返工=subCode:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("在线返工失败！", e);
	             getSysLogService().error(method, methodName, e.toString());
	             return ApiResponseResult.failure("在线返工失败！");
	        }
	    }
	  
	  @ApiOperation(value="在线返工", notes="在线返工", hidden = true)
	    @RequestMapping(value = "/search", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult search(@RequestBody Map<String, Object> params) {
	        String method = "produce/rework/search";String methodName ="在线返工";
	        try {
	        	String startTime = params.get("startTime") == null?"":params.get("startTime").toString();
	        	String endTime = params.get("endTime") == null?"":params.get("endTime").toString();
	        	String taskNo = params.get("taskNo") == null?"":params.get("taskNo").toString();
	        	String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
	            ApiResponseResult result = onlineReworkService.subCode(startTime,endTime,taskNo,barcode);
	            logger.debug("在线返工=search:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("在线返工失败！", e);
	             getSysLogService().error(method, methodName, e.toString());
	             return ApiResponseResult.failure("在线返工失败！");
	        }
	    }
}
