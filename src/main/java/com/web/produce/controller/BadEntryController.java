package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.web.produce.service.BadEntryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;

@Api(description = "不良录入模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/bad_entry")
public class BadEntryController extends WebController {

	private String module = "不良录入";

	 @Autowired
	 private BadEntryService badEntryService;
	 
	 @ApiOperation(value = "不良录入页", notes = "不良录入页", hidden = true)
	    @RequestMapping(value = "/toBadEntry")
	    public String toBadEntry(){
	        return "/web/produce/bad_entry/bad_entry";
	    }
	 
	  @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
	    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getTaskNo(String keyword) {
	        String method = "produce/bad_entry/getTaskNo";String methodName ="获取指令单信息";
	        try {
	            ApiResponseResult result = badEntryService.getTaskNo(keyword);
	            //logger.debug("获取指令单信息=getTaskNo:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取指令单信息失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取指令单信息失败！");
	        }
	    }
	  
	  @ApiOperation(value="获取不良信息", notes="获取不良信息", hidden = true)
	    @RequestMapping(value = "/getBadInfo", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getBadInfo(@RequestBody Map<String, Object> params) {
	        String method = "produce/bad_entry/getBadInfo";String methodName ="获取不良信息";
	        try {
	        	String keyword = params.get("keyword") == null?"":params.get("keyword").toString();
	            ApiResponseResult result = badEntryService.getBadInfo(keyword);
	            logger.debug("获取不良信息信息=getBadInfo:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取不良信息失败！", e);
	             getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	             return ApiResponseResult.failure("获取不良信息失败！");
	        }
	    }
	  
	  @ApiOperation(value="条码扫描", notes="条码扫描", hidden = true)
	    @RequestMapping(value = "/checkBarCode", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult checkBarCode(@RequestBody Map<String, Object> params) {
	        String method = "produce/bad_entry/checkBarCode";String methodName ="条码扫描";
	        try {
	        	String taskNo = params.get("taskNo").toString();
	        	String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
	            ApiResponseResult result = badEntryService.checkBarCode(taskNo,barcode);
	            logger.debug("条码扫描=checkBarCode:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("条码扫描失败！", e);
	             getSysLogService().error(module,method, methodName, params+";"+e.toString());
	             return ApiResponseResult.failure("条码扫描失败！");
	        }
	    }
	  
	  @ApiOperation(value="保存不良", notes="保存不良", hidden = true)
	    @RequestMapping(value = "/saveBad", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult saveBad(@RequestBody Map<String, Object> params) {
	        String method = "produce/bad_entry/saveBad";String methodName ="保存不良";
	        try {
	        	String taskNo = params.get("taskNo").toString();
	        	String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
	        	int qty = params.get("qty") == null?0:Integer.parseInt(params.get("qty").toString());
	        	String defCode = params.get("defCode") == null?"":params.get("defCode").toString();
	        	String memo = params.get("memo") == null?"":params.get("memo").toString();
	            ApiResponseResult result = badEntryService.saveBad(taskNo,barcode,
	        			qty,defCode,memo);
	            logger.debug("保存不良=saveBad:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("保存不良失败！", e);
	             getSysLogService().error(module,method, methodName,params+":"+ e.toString());
	             return ApiResponseResult.failure("保存不良失败！");
	        }
	    }
	  
	  @ApiOperation(value="删除不良", notes="删除不良", hidden = true)
	    @RequestMapping(value = "/deleteBad", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult deleteBad(@RequestBody Map<String, Object> params) {
	        String method = "produce/bad_entry/deleteBad";String methodName ="删除不良";
	        try {
	        	String recordId = params.get("recordId").toString();
	            ApiResponseResult result = badEntryService.deleteBad(recordId);
	            logger.debug("删除不良=deleteBad:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("删除不良失败！", e);
	             getSysLogService().error(module,method, methodName,params+":"+ e.toString());
	             return ApiResponseResult.failure("删除不良失败！");
	        }
	    }
	  
	  @ApiOperation(value="根据指令单获取扫描信息", notes="根据指令单获取扫描信息", hidden = true)
	    @RequestMapping(value = "/getDetailByTask", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getDetailByTask(String taskNo) {
	        String method = "/product/bad_entry/getDetailByTask";String methodName ="根据指令单获取扫描信息";
	        try {
	            ApiResponseResult result = badEntryService.getDetailByTask(taskNo);
	            logger.debug("根据指令单获取扫描信息=getDetailByTask:");
//	            getSysLogService().success(module,method, methodName, "指令单号:"+taskNo);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("根据指令单获取扫描信息失败！", e);
	             getSysLogService().error(module,method, methodName,"指令单号:"+taskNo+ e.toString());
	             return ApiResponseResult.failure("根据指令单获取扫描信息失败！");
	        }
	    }
	  
	  @ApiOperation(value = "获取历史列表", notes = "获取历史列表")
	    @RequestMapping(value = "/getHistoryList", method = RequestMethod.GET)
	    @ResponseBody
		public ApiResponseResult getHistoryList(
				@RequestParam(value = "hkeywork", required = false) String hkeywork,
				@RequestParam(value = "hStartTime", required = false) String hStartTime,
				@RequestParam(value = "hEndTime", required = false) String hEndTime){
		  String method = "/product/bad_entry/getHistoryList";String methodName ="获取历史列表";
	        try {
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result =badEntryService.getHistoryList(hkeywork,hStartTime,hEndTime, super.getPageRequest(sort));
	            logger.debug(methodName+"=getList:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error(methodName+"失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure(methodName+"失败！");
	        }
		}
}
