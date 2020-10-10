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
import com.web.produce.service.CheckCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "小码校验模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/check_code")
public class CheckCodeController extends WebController {

	 @Autowired
	 private CheckCodeService checkCodeService;
	 
	 @ApiOperation(value = "生产投料页", notes = "生产投料页", hidden = true)
	    @RequestMapping(value = "/toCheckCode")
	    public String toCheckCode(){
	        return "/web/produce/check_code";
	    }
	 
	  @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
	    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getTaskNo(String keyword) {
	        String method = "produce/check_code/getTaskNo";String methodName ="获取指令单信息";
	        try {
	            ApiResponseResult result = checkCodeService.getTaskNo(keyword);
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
	  @ApiOperation(value="小码校验", notes="小码校验", hidden = true)
	    @RequestMapping(value = "/subCode", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult subCode(@RequestBody Map<String, Object> params) {
	        String method = "produce/check_code/subCode";String methodName ="小码校验";
	        try {
	        	String taskNo = params.get("taskNo").toString();
	        	String barcode1 = params.get("barcode1") == null?"":params.get("barcode1").toString();
	        	String barcode2 = params.get("barcode2") == null?"":params.get("barcode2").toString();
	            ApiResponseResult result = checkCodeService.subCode(taskNo,barcode1,barcode2);
	            logger.debug("小码校验=subCode:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("小码校验失败！", e);
	             getSysLogService().error(method, methodName, e.toString());
	             return ApiResponseResult.failure("小码校验失败！");
	        }
	    }
}
