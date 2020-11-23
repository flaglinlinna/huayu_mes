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
import com.web.report.service.LineDayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "各线每日生产报表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/line_day")
public class LineDayController extends WebController {

	private String module = "各线每日生产报表";

	@Autowired
	private LineDayService lineDayService;

	/*@ApiOperation(value = "检验批次报表", notes = "检验批次报表", hidden = true)
	@RequestMapping(value = "/toCheckBatch")
	public String toCheckBatch() {
		return "/web/report/check_batch";
	}*/
	@ApiOperation(value = "各线每日生产报表", notes = "各线每日生产报表", hidden = true)
	@RequestMapping(value = "/toLineDay", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toLineDay() {
		ModelAndView mav = new ModelAndView();
		 mav.addObject("Depart", this.getDeptInfo(""));
		mav.setViewName("/web/report/line_day");// 返回路径
		return mav;
	}

	@ApiOperation(value = "获取部门信息", notes = "获取部门信息", hidden = true)
	@RequestMapping(value = "/getDeptInfo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getDeptInfo(String keyword) {
		String method = "report/line_day/getDeptInfo";
		String methodName = "获取部门信息";
		try {
			ApiResponseResult result = lineDayService.getDeptInfo(keyword);
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
		String method = "report/line_day/getItemList";
		String methodName = "获取物料信息";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = lineDayService.getItemList(keyword, super.getPageRequest(sort));
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
	
	@ApiOperation(value = "各线每日生产报表", notes = "各线每日生产报表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(@RequestParam(value = "dates", required = false) String dates,
												 @RequestParam(value = "deptId", required = false) String deptId,
												 @RequestParam(value = "itemNo", required = false) String itemNo) {
		String method = "report/line_day/getList";
		String methodName = "各线每日生产报表";
		try {
			
			String[] date = {"",""};
			if(StringUtils.isNotEmpty(dates)){
				date = dates.split(" - ");
			}
			Sort sort = Sort.unsorted();
			ApiResponseResult result = lineDayService.getList(date[0],date[1],
					deptId, itemNo,this.getIpAddr(),super.getPageRequest(sort));
			logger.debug(methodName+"=getList:");
			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}
}
