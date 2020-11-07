package com.web.report.controller;

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
import com.web.produce.service.ReworkService;
import com.web.report.service.CheckBatchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "检验批次报表(FQC)")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/batch")
public class CheckBatchController extends WebController {

	private String module = "检验批次报表(FQC)";

	@Autowired
	private CheckBatchService checkBatchService;

	@ApiOperation(value = "检验批次报表", notes = "检验批次报表", hidden = true)
	@RequestMapping(value = "/toCheckBatch")
	public String toCheckBatch() {
		return "/web/report/batch";
	}

	@ApiOperation(value = "获取部门信息", notes = "获取部门信息", hidden = true)
	@RequestMapping(value = "/getDeptInfo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getTaskNo(String keyword) {
		String method = "report/batch/getDeptInfo";
		String methodName = "获取部门信息";
		try {
			ApiResponseResult result = checkBatchService.getDeptInfo(keyword);
			logger.debug("获取部门信息=getDeptInfo:");
			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取部门信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取部门信息失败！");
		}
	}
	
	@ApiOperation(value = "获取物料信息", notes = "获取物料信息", hidden = true)
	@RequestMapping(value = "/getItemList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getItemList(String keyword) {
		String method = "report/batch/getItemList";
		String methodName = "获取物料信息";
		try {
			ApiResponseResult result = checkBatchService.getItemList(keyword);
			logger.debug("获取物料信息=getItemList:");
			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取物料信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取物料信息失败！");
		}
	}
	
	@ApiOperation(value = "获取检验批次报表(FQC)", notes = "获取获取检验批次报表(FQC)", hidden = true)
	@RequestMapping(value = "/getCheckBatchReport", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCheckBatchReport(@RequestBody Map<String, Object> params) {
		String method = "report/batch/getCheckBatchReport";
		String methodName = "获取获取检验批次报表(FQC)";
		try {
			String beginTime = params.get("beginTime") == null?"":params.get("beginTime").toString();
        	String endTime = params.get("endTime") == null?"":params.get("endTime").toString();
        	String deptId = params.get("deptId") == null?"":params.get("deptId").toString();
        	String itemNo = params.get("itemNo") == null?"":params.get("itemNo").toString();
			ApiResponseResult result = checkBatchService.getCheckBatchReport(beginTime,endTime,
					deptId, itemNo);
			logger.debug("获取获取检验批次报表(FQC)=getCheckBatchReport:");
			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取获取检验批次报表(FQC)失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取获取检验批次报表(FQC)失败！");
		}
	}
}
