package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.QualInspectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "品质检验模块")
@RestController
@RequestMapping(value = "produce/inspect")
public class QualInspectController extends WebController {

	@Autowired
	private QualInspectService inspectService;

	/*
	 * //pda应用，无页面
	 * 
	 * @ApiOperation(value = "品质检验", notes = "品质检验", hidden = true)
	 * 
	 * @RequestMapping(value = "/toQualInspect") public String toQualInspect(){
	 * return "/web/produce/inspect/inspect"; }
	 */

	@ApiOperation(value = "PDA-获取检验节点列表", notes = "PDA-获取检验节点列表", hidden = true)
	@RequestMapping(value = "/getProcList", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult getProcList(@RequestParam(value = "company") String company,
			@RequestParam(value = "factory") String factory,
			@RequestParam(value = "keyword") String keyword) {
		String method = "produce/inspect/getProcList";
		String methodName = "PDA-获取检验节点列表";
		try {
			ApiResponseResult result = inspectService.getProcList(company, factory, keyword);
			logger.debug("PDA-获取检验节点列表=getProcList:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取检验节点列表失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取检验节点列表失败！");
		}
	}

	@ApiOperation(value = "PDA-扫描条码", notes = "PDA-扫描条码", hidden = true)
	@RequestMapping(value = "/scanBarcode", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult scanBarcode(@RequestParam(value = "company") String company,
			@RequestParam(value = "factory") String factory,
			@RequestParam(value = "user_id") String user_id,
			@RequestParam(value = "proc") String proc,
			@RequestParam(value = "barcode") String barcode) {
		String method = "produce/inspect/scanBarcode";
		String methodName = "PDA-扫描条码";
		try {
			ApiResponseResult result = inspectService.scanBarcode(company,factory,user_id,proc, barcode);
			logger.debug("PDA-扫描条码=scanBarcode:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PDA-扫描条码失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("PDA-扫描条码失败！");
		}
	}

	@ApiOperation(value = "PDA-获取责任部门列表", notes = "PDA-获取责任部门列表", hidden = true)
	@RequestMapping(value = "/getDepatrList", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult getDepatrList(@RequestParam(value = "company") String company,
			@RequestParam(value = "factory") String factory,
			@RequestParam(value = "keyword") String keyword) {
		String method = "produce/inspect/getDepatrList";
		String methodName = "PDA-获取责任部门列表";
		try {
			ApiResponseResult result = inspectService.getDepatrList(factory,company, keyword);
			logger.debug("PDA-获取责任部门列表=getDepatrList:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PDA-获取责任部门列表失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("PDA-获取责任部门列表失败！");
		}
	}
	
	
	@ApiOperation(value = "PDA-获取不良内容列表", notes = "PDA-获取不良内容列表", hidden = true)
	@RequestMapping(value = "/getBadList", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getBadList(@RequestParam(value = "company") String company,
			@RequestParam(value = "factory") String factory,
			@RequestParam(value = "keyword") String keyword) {
		String method = "produce/inspect/getBadList";
		String methodName = "PDA-获取不良内容列表";
		try {
			ApiResponseResult result = inspectService.getBadList(company,factory,keyword);
			logger.debug("PDA-获取不良内容列表=getBadList:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PDA-获取不良内容列表失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("PDA-获取不良内容列表失败！");
		}
	}

	@ApiOperation(value = "PDA-保存PDA品质检查数据", notes = "PDA-保存PDA品质检查数据", hidden = true)
	@RequestMapping(value = "/saveData", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult saveData(@RequestParam(value = "factory") String factory,
			@RequestParam(value = "company") String company,
			@RequestParam(value = "user_id") String user_id,
			@RequestParam(value = "proc") String proc,
			@RequestParam(value = "barcodeList") String barcodeList,
			@RequestParam(value = "checkTotal") int checkTotal,
			@RequestParam(value = "badTotal") int badTotal,
			@RequestParam(value = "chkResult") String chkResult,
			@RequestParam(value = "departCode") String departCode,
			@RequestParam(value = "badList") String badList) {
		String method = "produce/inspect/saveData";
		String methodName = "PDA-保存PDA品质检查数据";
		try {
			ApiResponseResult result = inspectService.saveData(factory,company,user_id, proc, 
					barcodeList, checkTotal, badTotal, chkResult,departCode, badList);
			logger.debug("PDA-保存PDA品质检查数据=saveData:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PDA-保存PDA品质检查数据失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("PDA-保存PDA品质检查数据失败！");
		}
	}
}
