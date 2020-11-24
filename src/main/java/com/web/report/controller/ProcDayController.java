package com.web.report.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.report.service.ProcDayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "过程检验日报表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/proc_day")
public class ProcDayController extends WebController {

	private String module = "过程检验日报表";

	@Autowired
	private ProcDayService procDayService;

	@ApiOperation(value = "过程检验日报表", notes = "过程检验日报表", hidden = true)
	@RequestMapping(value = "/toProcDay", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toProcDay() {
		ModelAndView mav = new ModelAndView();
		 mav.addObject("Depart", this.getDeptInfo(""));
		mav.setViewName("/web/report/proc_day");// 返回路径
		return mav;
	}

	@ApiOperation(value = "获取部门信息", notes = "获取部门信息", hidden = true)
	@RequestMapping(value = "/getDeptInfo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getDeptInfo(String keyword) {
		String method = "report/proc_day/getDeptInfo";
		String methodName = "获取部门信息";
		try {
			ApiResponseResult result = procDayService.getDeptInfo(keyword);
			logger.debug("获取部门信息=getDeptInfo:");
//			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取部门信息失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取部门信息失败！");
		}
	}
	
	@ApiOperation(value = "获取物料信息", notes = "获取物料信息", hidden = true)
	@RequestMapping(value = "/getItemList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getItemList(String keyword) {
		String method = "report/proc_day/getItemList";
		String methodName = "获取物料信息";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = procDayService.getItemList(keyword, super.getPageRequest(sort));
			logger.debug("获取物料信息=getItemList:");
//			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取物料信息失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取物料信息失败！");
		}
	}
	
	@ApiOperation(value = "获取过程检验日报表", notes = "获取过程检验日报表", hidden = true)
	@RequestMapping(value = "/getReport", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getReport(@RequestParam(value = "dates", required = false) String dates,
												 @RequestParam(value = "deptId", required = false) String deptId,
												 @RequestParam(value = "itemNo", required = false) String itemNo) {
		String method = "report/proc_day/getReport";
		String methodName = "获取过程检验日报表";
		String param = "日期:"+dates +";部门Id:" +deptId +";制令单："+itemNo;
		try {
			
			String[] date = {"",""};
			if(StringUtils.isNotEmpty(dates)){
				date = dates.split(" - ");
			}
			Sort sort = Sort.unsorted();
			ApiResponseResult result = procDayService.getReport(date[0],date[1],
					deptId, itemNo,super.getPageRequest(sort));
			logger.debug(methodName+"=getReport:");
//			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, param+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}
	
	@ApiOperation(value = "获取品质检验日报表", notes = "获取品质检验日报表", hidden = true)
	@RequestMapping(value = "/getDetailReport", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getDetailReport(@RequestParam(value = "user_id", required = false) String user_id,
												 @RequestParam(value = "dep_id", required = false) String dep_id,
												 @RequestParam(value = "proc_no", required = false) String proc_no,
												 @RequestParam(value = "fdate", required = false) String fdate) {
		String method = "report/proc_day/getDetailReport";
		String methodName = "获取品质检验日报表";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = procDayService.getDetailReport(user_id,dep_id,proc_no, fdate,super.getPageRequest(sort));
			logger.debug(methodName+"=getReport:");
//			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}
}
