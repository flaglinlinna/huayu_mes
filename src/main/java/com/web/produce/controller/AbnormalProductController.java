package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.AbnormalHours;
import com.web.produce.entity.AbnormalProduct;
import com.web.produce.service.AbnormalProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "生产异常模块")
@CrossOrigin
@ControllerAdvice
// @RestController
@Controller
@RequestMapping(value = "abnormalProduct")
public class AbnormalProductController extends WebController {

	private String module = "生产异常信息";
	@Autowired
	private AbnormalProductService abnormalHoursService;

	@ApiOperation(value = "生产异常信息", notes = "生产异常信息列表页", hidden = true)
	@RequestMapping(value = "/toAbnormalProduct")
	public String toAbnormalHours() {
		return "/web/produce/dev_clock/abnormal_product";
	}

	@ApiOperation(value = "获取生产异常信息列表", notes = "获取生产异常信息列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword) {
		String method = "abnormalProduct/getList";
		String methodName = "获取生产异常信息列表";
		try {
			System.out.println(keyword);
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = abnormalHoursService.getList(keyword, super.getPageRequest(sort));
			logger.debug(methodName+"=getList:");
			getSysLogService().success(module,method, methodName, keyword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName, keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "新增生产异常信息", notes = "新增生产异常信息", hidden = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult add(@RequestBody AbnormalProduct abnormalProduct) {
		String method = "abnormalProduct/add";
		String methodName = "新增生产异常信息";
		try {
			ApiResponseResult result = abnormalHoursService.add(abnormalProduct);
			logger.debug(methodName+"=add:");
			getSysLogService().success(module,method, methodName, abnormalProduct.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName, abnormalProduct.toString()+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "修改生产异常信息", notes = "修改生产异常信息", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody AbnormalProduct abnormalProduct) {
		String method = "abnormalProduct/edit";
		String methodName = "修改生产异常信息";
		try {
			ApiResponseResult result = abnormalHoursService.edit(abnormalProduct);
			logger.debug(methodName+"=edit:");
			getSysLogService().success(module,method, methodName, abnormalProduct.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName, abnormalProduct.toString()+";"+ e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}
	
	@ApiOperation(value = "删除异常工时登记数据记录", notes = "删除异常工时登记记录", hidden = true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "abnormalProduct/delete";
		String methodName = "删除生产异常信息记录";
		try {
			String id = params.get("id").toString();
			ApiResponseResult result = abnormalHoursService.delete(id);
			logger.debug(methodName+"=delete:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "获取异常原因", notes = "获取异常原因", hidden = true)
	@RequestMapping(value = "/getErrorInfo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getErrorInfo() {
		String method = "abnormalProduct/getErrorInfo";
		String methodName = "获取异常原因";
		try {
			ApiResponseResult result = abnormalHoursService.getErrorInfo();
			logger.debug(methodName+"=getEmpInfo:");
			getSysLogService().success(module,method, methodName,"");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "获取指令单信息", notes = "获取指令单信息", hidden = true)
	@RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getTaskNo(String keyword) {
		String method = "produce/abnormal/getTaskNo";
		String methodName = "获取指令单信息";
		try {
			ApiResponseResult result = abnormalHoursService.getTaskNo(keyword);
			logger.debug("获取指令单信息=getTaskNo:");
			getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取指令单信息失败！", e);
			getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+ e.toString());
			return ApiResponseResult.failure("获取指令单信息失败！");
		}
	}
	
	@ApiOperation(value = "获取指令单详细信息", notes = "获取指令单详细信息", hidden = true)
	@RequestMapping(value = "/getTaskNoInfo", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getTaskNoInfo(@RequestBody Map<String, Object> params) {
		String method = "produce/abnormal/getTaskNoInfo";
		String methodName = "获取指令单详细信息";
		try {
			String taskNo = params.get("taskNo") == null?"":params.get("taskNo").toString();
			ApiResponseResult result = abnormalHoursService.getTaskNoInfo(taskNo);
			logger.debug("获取指令单详细信息=getTaskNoInfo:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取指令单详细信息失败！", e);
			getSysLogService().error(module,method, methodName, params+";"+e.toString());
			return ApiResponseResult.failure("获取指令单详细信息失败！");
		}
	}
}
