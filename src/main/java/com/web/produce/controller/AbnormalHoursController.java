package com.web.produce.controller;

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
import com.web.produce.entity.AbnormalHours;
import com.web.produce.entity.DevClock;
import com.web.produce.service.AbnormalHoursService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "异常工时登记数据模块")
@CrossOrigin
@ControllerAdvice
// @RestController
@Controller
@RequestMapping(value = "produce/abnormal")
public class AbnormalHoursController extends WebController {

	@Autowired
	private AbnormalHoursService abnormalHoursService;

	@ApiOperation(value = "异常工时登记数据表结构", notes = "异常工时登记数据表结构" + AbnormalHours.TABLE_NAME)
	@RequestMapping(value = "/getAbnormalHours", method = RequestMethod.GET)
	@ResponseBody
	public AbnormalHours getAbnormalHours() {
		return new AbnormalHours();
	}

	@ApiOperation(value = "异常工时登记数据列表页", notes = "异常工时登记数据列表页", hidden = true)
	@RequestMapping(value = "/toAbnormalHours")
	public String toAbnormalHours() {
		return "/web/produce/dev_clock/abnormal";
	}

	@ApiOperation(value = "获取异常工时登记数据列表", notes = "获取异常工时登记数据列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword) {
		String method = "produce/abnormal/getList";
		String methodName = "获取异常工时登记数据列表";
		try {
			System.out.println(keyword);
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = abnormalHoursService.getList(keyword, super.getPageRequest(sort));
			logger.debug("获取异常工时登记数据列表=getList:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取异常工时登记数据列表失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取异常工时登记数据列表失败！");
		}
	}

	@ApiOperation(value = "新增补卡数据记录", notes = "新增补卡数据记录", hidden = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult add(@RequestBody AbnormalHours abnormalHours) {
		String method = "produce/abnormal/add";
		String methodName = "新增补卡数据记录";
		try {
			ApiResponseResult result = abnormalHoursService.add(abnormalHours);
			logger.debug("新增补卡数据记录=add:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("补卡数据记录新增失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("补卡数据记录新增失败！");
		}
	}

	@ApiOperation(value = "修改补卡数据记录", notes = "修改补卡数据记录", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody AbnormalHours abnormalHours) {
		String method = "produce/abnormal/edit";
		String methodName = "修改补卡数据记录";
		try {
			ApiResponseResult result = abnormalHoursService.edit(abnormalHours);
			logger.debug("修改补卡数据记录=edit:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改补卡数据记录失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("修改补卡数据记录失败！");
		}
	}

	@ApiOperation(value = "根据ID获取补卡数据记录", notes = "根据ID获取补卡数据记录", hidden = true)
	@RequestMapping(value = "/getAbnormalHours", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getAbnormalHours(@RequestBody Map<String, Object> params) {
		String method = "produce/abnormal/getAbnormalHours";
		String methodName = "根据ID获取补卡数据记录";
		long id = Long.parseLong(params.get("id").toString());
		try {
			ApiResponseResult result = abnormalHoursService.getAbnormalHours(id);
			logger.debug("根据ID获取补卡数据记录=getAbnormalHours:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据ID获取补卡数据记录失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取补卡数据记录失败！");
		}
	}

	@ApiOperation(value = "删除补卡数据记录", notes = "删除补卡数据记录", hidden = true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "produce/abnormal/delete";
		String methodName = "删除补卡数据记录";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = abnormalHoursService.delete(id);
			logger.debug("删除补卡数据记录=delete:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除补卡数据记录失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("删除补卡数据记录失败！");
		}
	}

	@ApiOperation(value = "获取员工数据", notes = "获取员工数据", hidden = true)
	@RequestMapping(value = "/getEmpInfo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getEmpInfo(String keyword) {
		String method = "produce/abnormal/getEmpInfo";
		String methodName = "获取员工数据";
		try {
			ApiResponseResult result = abnormalHoursService.getEmpInfo(keyword);
			logger.debug("获取员工数据=getEmpInfo:");
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取员工数据失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取员工数据失败！");
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
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取指令单信息失败！", e);
			getSysLogService().error(method, methodName, e.toString());
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
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取指令单详细信息失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取指令单详细信息失败！");
		}
	}
}
