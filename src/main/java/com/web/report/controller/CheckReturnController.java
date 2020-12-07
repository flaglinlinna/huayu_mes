package com.web.report.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.ReworkService;
import com.web.report.service.CheckReturnService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "检验批数及退货报表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/check_return")
public class CheckReturnController extends WebController {

	private String module = "检验批数及退货报表";

	@Autowired
	private CheckReturnService checkReturnService;

	/*@ApiOperation(value = "检验批数及退货报表", notes = "检验批数及退货报表", hidden = true)
	@RequestMapping(value = "/toCheckReturn")
	public String toCheckReturn() {
		return "/web/report/check_return";
	}*/
	@ApiOperation(value = "检验批数及退货报表", notes = "检验批数及退货报表", hidden = true)
	@RequestMapping(value = "/toCheckReturn", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCheckReturn() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("depart", this.getDeptInfo(""));
		mav.setViewName("/web/report/check_return");// 返回路径
		return mav;
	}

	@ApiOperation(value = "获取部门信息", notes = "获取部门信息", hidden = true)
	@RequestMapping(value = "/getDeptInfo", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getDeptInfo(String keyword) {
		String method = "report/check_return/getDeptInfo";
		String methodName = "获取部门信息";
		try {
			ApiResponseResult result = checkReturnService.getDeptInfo(keyword);
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
	
	
	@ApiOperation(value = "获取检验批数及退货报表", notes = "获取获取检验批数及退货报表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String month, String deptId) {
		String method = "report/check_return/getList";
		String methodName = "获取获取检验批数及退货报表";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = checkReturnService.getList(month,
					deptId,super.getPageRequest(sort));
			logger.debug("获取获取检验批数及退货报表=getList:");
			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取获取检验批数及退货报表失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取获取检验批数及退货报表失败！");
		}
	}
}
