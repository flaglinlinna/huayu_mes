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
	/*
	 * @ApiOperation(value="获取检验节点列表", notes="获取检验节点列表", hidden = true)
	 * 
	 * @RequestMapping(value = "/getProcList", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public ApiResponseResult getProcList(String keyword) {
	 * String method = "produce/inspect/getProcList";String methodName
	 * ="获取检验节点列表"; try { ApiResponseResult result =
	 * inspectService.getProcList(company,facoty,keyword);
	 * logger.debug("获取检验节点列表=getProcList:"); getSysLogService().success(method,
	 * methodName, null); return result; } catch (Exception e) {
	 * e.printStackTrace(); logger.error("获取检验节点列表失败！", e);
	 * getSysLogService().error(method, methodName, e.toString()); return
	 * ApiResponseResult.failure("获取检验节点列表失败！"); } }
	 */

	@ApiOperation(value = "PDA-获取检验节点列表", notes = "PDA-获取检验节点列表")
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

	@ApiOperation(value = "扫描条码", notes = "扫描条码", hidden = true)
	@RequestMapping(value = "/scanBarcode", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult scanBarcode(@RequestBody Map<String, Object> params) {
		String method = "produce/inspect/scanBarcode";
		String methodName = "扫描条码";
		try {
			String proc = params.get("proc").toString();
			String barcode = params.get("barcode") == null ? "" : params.get("barcode").toString();

			ApiResponseResult result = inspectService.scanBarcode(proc, barcode);
			logger.debug("扫描条码=scanBarcode:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("扫描条码失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("扫描条码失败！");
		}
	}

	@ApiOperation(value = "获取不良内容列表", notes = "获取不良内容列表", hidden = true)
	@RequestMapping(value = "/getBadList", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getBadList(String keyword) {
		String method = "produce/inspect/getBadList";
		String methodName = "获取不良内容列表";
		try {
			ApiResponseResult result = inspectService.getBadList(keyword);
			logger.debug("获取不良内容列表=getBadList:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取不良内容列表失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取不良内容列表失败！");
		}
	}

	@ApiOperation(value = "保存PDA品质检查数据", notes = "保存PDA品质检查数据", hidden = true)
	@RequestMapping(value = "/saveData", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult saveData(@RequestBody Map<String, Object> params) {
		String method = "produce/inspect/saveData";
		String methodName = "保存PDA品质检查数据";
		try {
			String proc = params.get("proc") == null ? "" : params.get("proc").toString();
			String barcodeList = params.get("barcodeList") == null ? "" : params.get("barcodeList").toString();
			int checkTotal = params.get("checkTotal") == null ? 0 : (int) params.get("checkTotal");
			int badTotal = params.get("badTotal") == null ? 0 : (int) params.get("badTotal");
			String chkResult = params.get("chkResult") == null ? "" : params.get("chkResult").toString();
			String departCode = params.get("departCode") == null ? "" : params.get("departCode").toString();
			String badList = params.get("badList") == null ? "" : params.get("badList").toString();

			ApiResponseResult result = inspectService.saveData(proc, barcodeList, checkTotal, badTotal, chkResult,
					departCode, badList);
			logger.debug("保存PDA品质检查数据=saveData:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存PDA品质检查数据失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("保存PDA品质检查数据失败！");
		}
	}
}
