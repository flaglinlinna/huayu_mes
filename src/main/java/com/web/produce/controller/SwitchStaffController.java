package com.web.produce.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.web.produce.service.SwitchStaffService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "在线人员调整")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/switch_staff")
public class SwitchStaffController extends WebController {

	private String module = "在线人员调整";
	@Autowired
	private SwitchStaffService switchStaffService;

	@ApiOperation(value = "在线人员调整页", notes = "在线人员调整页", hidden = true)
	@RequestMapping(value = "/toSwitchStaff")
	public String toSwitchStaff() {
		return "/web/produce/switch_staff/switch_staff";
	}

	@ApiOperation(value = "获取在线人员列表信息", notes = "获取在线人员列表信息", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(@RequestParam(value = "dates", required = false) String dates,
										@RequestParam(value = "keyword", required = false) String keyword) {
		String method = "/switch_staff/getList";
		String methodName = "获取在线人员列表信息";
		try {
			String[] date = {"",""};
			if(StringUtils.isNotEmpty(dates)){
				date = dates.split(" - ");
			}
			Sort sort = Sort.unsorted();
			ApiResponseResult result = switchStaffService.getList(date[0],date[1],keyword,super.getPageRequest(sort));
			logger.debug("获取在线人员列表信息=getTaskNo:");
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取在线人员列表信息失败！", e);
			getSysLogService().error(module, method, methodName,
					"关键字" + keyword == null ? ";" : keyword + ";" + e.toString());
			return ApiResponseResult.failure("获取在线人员列表信息失败！");
		}
	}

	@ApiOperation(value = "获取原指令单信息", notes = "获取原指令单信息", hidden = true)
	@RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getTaskNo(String keyword) {
		String method = "/switch_staff/getTaskNo";
		String methodName = "获取原指令单信息";
		try {
			ApiResponseResult result = switchStaffService.getTaskNo(keyword);
			logger.debug("获取指令单信息=getTaskNo:");
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取原指令单信息失败！", e);
			getSysLogService().error(module, method, methodName,
					"关键字" + keyword == null ? ";" : keyword + ";" + e.toString());
			return ApiResponseResult.failure("获取原指令单信息失败！");
		}
	}
	
	@ApiOperation(value = "获取新指令单信息", notes = "获取新指令单信息", hidden = true)
	@RequestMapping(value = "/getNewTaskNo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getNewTaskNo(String keyword) {
		String method = "/switch_staff/getNewTaskNo";
		String methodName = "获取新指令单信息";
		try {
			ApiResponseResult result = switchStaffService.getNewTaskNo(keyword);
			logger.debug("获取新指令单信息=getNewTaskNo:");
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取新指令单信息失败！", e);
			getSysLogService().error(module, method, methodName,
					"关键字" + keyword == null ? ";" : keyword + ";" + e.toString());
			return ApiResponseResult.failure("获取新指令单信息失败！");
		}
	}
	
	@ApiOperation(value = "获取待调整人员列表", notes = "获取待调整人员列表", hidden = true)
	@RequestMapping(value = "/getTaskNoEmp", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getTaskNoEmp(String aff_id) {
		String method = "/switch_staff/getTaskNoEmp";
		String methodName = "获取待调整人员列表";
		try {		
			//String taskNo = params.get("taskNo") == null?"":params.get("taskNo").toString();
			//String workDate = params.get("workDate") == null?"":params.get("workDate").toString();
			Sort sort =  Sort.unsorted();
			ApiResponseResult result = switchStaffService.getTaskNoEmp(aff_id,super.getPageRequest(sort));
			logger.debug("获取待调整人员列表=getTaskNoEmp:");
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取待调整人员列表失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取待调整人员列表失败！");
		}
	}
	
	@ApiOperation(value = "获取产线信息", notes = "获取产线信息", hidden = true)
	@RequestMapping(value = "/getLine", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getLine(String keyword) {
		String method = "/switch_staff/getLine";
		String methodName = "获取产线信息";
		try {
			ApiResponseResult result = switchStaffService.getLine(keyword);
			logger.debug("获取产线信息=getLine:");
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线信息失败！", e);
			getSysLogService().error(module, method, methodName,
					"关键字" + keyword == null ? ";" : keyword + ";" + e.toString());
			return ApiResponseResult.failure("获取产线信息失败！");
		}
	}
	
	@ApiOperation(value = "获取班次信息", notes = "获取班次信息", hidden = true)
	@RequestMapping(value = "/getClassType", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getClassType() {
		String method = "/switch_staff/getClassType";
		String methodName = "获取班次信息";
		try {
			ApiResponseResult result = switchStaffService.getClassType();
			logger.debug("获取班次信息=getClassType:");
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取班次信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取班次信息失败！");
		}
	}
	
	@ApiOperation(value = "执行人员调整", notes = "执行人员调整", hidden = true)
	@RequestMapping(value = "/doSwitch", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult doSwitch(String lastTaskNo,String lastTaskNo_id,String lastDatetimeEnd,
			String newTaskNo, String newLineId,String newHourType, String newClassId,
			String newDatetimeBegin, String empList,String switchType) {
		String method = "/switch_staff/doSwitch";
		String methodName = "执行人员调整";
		try {		
			Sort sort =  Sort.unsorted();
			ApiResponseResult result = switchStaffService.doSwitch(lastTaskNo_id,lastDatetimeEnd,
					newTaskNo, newLineId,newHourType, newClassId,newDatetimeBegin, empList,switchType,super.getPageRequest(sort));
			logger.debug("执行人员调整=doSwitch:");
			if(result.isResult()) {
				getSysLogService().success(module, method, methodName, "类型:" + switchType + ";原制令单号:" + lastTaskNo + ";下线时间:" + lastDatetimeEnd + ";新单号:" + newTaskNo + ";上线时间" + newDatetimeBegin);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("执行人员调整失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("执行人员调整失败！");
		}
	}
}
