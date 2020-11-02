package com.web.kanban.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.utils.UserUtil;
import com.web.kanban.service.KanbanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "看板")
@ApiIgnore
@CrossOrigin
@ControllerAdvice
// @RestController
@Controller
@RequestMapping(value = "/kanban")
public class kanbanController extends WebController {

	@Autowired
	private KanbanService kanbanService;

	@RequestMapping(value = "/toDemo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toDemo() {
		String method = "/kanban/toDemo";
		String methodName = "看板demo";
		ModelAndView mav = new ModelAndView();
		// mav.addObject("pname", p);
		mav.setViewName("/kanban/demo");// 返回路径
		return mav;
	}

	@RequestMapping(value = "/toIndex", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toIndex() {
		String method = "/kanban/toIndex";
		String methodName = "看板demo";
		ModelAndView mav = new ModelAndView();
		// mav.addObject("pname", p);
		mav.setViewName("/kanban/index");// 返回路径
		return mav;
	}

	@RequestMapping(value = "/toCjbg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbg() {
		String method = "/kanban/toCjbg";
		String methodName = "车间报工看板";
		ModelAndView mav = new ModelAndView();
		// mav.addObject("pname", p);
		mav.setViewName("/kanban/cjbg");// 返回路径
		return mav;
	}

	@RequestMapping(value = "/toCjbg1", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbg1(String line) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjbg1";
		String methodName = "车间报工看板";
		try {	
			ApiResponseResult result = kanbanService.getCjbgList("999",line,"","","");
			logger.debug("获取看板=toCjbg1:" + result);
			getSysLogService().success(method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.setViewName("/kanban/cjbg1");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("看板异常！", e);
			getSysLogService().error(method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toScdz", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toScdz(String line) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toScdz";
		String methodName = "生产电子看板";
		try {	
			ApiResponseResult result = kanbanService.getScdzList("999",line,"","","");
			logger.debug("获取生产电子看板=toScdz:" + result);
			getSysLogService().success(method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.setViewName("/kanban/scdz");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产电子看板异常！", e);
			getSysLogService().error(method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toZcbl", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toZcbl(String line) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toZcbl";
		String methodName = "制程不良看板";
		try {	
			ApiResponseResult result = kanbanService.getZcblList();
			logger.debug("制程不良看板=toZcbl:" + result);
			getSysLogService().success(method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.setViewName("/kanban/zcbl");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制程不良看板异常！", e);
			getSysLogService().error(method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toXlpm", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toXlpm(String line) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toXlpm";
		String methodName = "效率排名看板";
		try {	
			ApiResponseResult result = kanbanService.getXlpmList();
			logger.debug("效率排名看板=toXlpm:" + result);
			getSysLogService().success(method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.setViewName("/kanban/xlpm");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取效率排名看板异常！", e);
			getSysLogService().error(method,methodName,e.toString());
		}
		return mav;
	}
	

	
	
	
	
	@ApiOperation(value = "获取车间报工看板-部门信息", notes = "获取车间报工看板-部门信息", hidden = true)
	@RequestMapping(value = "/getCjbgDepList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCjbgDepList(String class_nos, String dep_id, String sdata, String edata) {
		String method = "/kanban/getCjbgDepList";
		String methodName = "获取车间报工看板-部门信息";
		try {
			ApiResponseResult result = kanbanService.getCjbgDepList();
			logger.debug("获取车间报工看板-部门信息=getCjbgList:" + result);
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取车间报工看板-部门信息失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取车间报工看板-部门信息失败！");
		}
	}

	@ApiOperation(value = "获取车间报工看板信息", notes = "获取车间报工看板信息", hidden = true)
	@RequestMapping(value = "/getCjbgList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCjbgList(String class_nos, String dep_id, String sdata, String edata) {
		String method = "/kanban/getCjbgList";
		String methodName = "获取车间报工看板信息";
		try {
			ApiResponseResult result = kanbanService.getCjbgList(class_nos, dep_id, sdata, edata, this.getIpAddr());
			logger.debug("获取车间报工看板信息=getCjbgList:" + result);
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取车间报工看板信息失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取车间报工看板信息失败！");
		}
	}

	@ApiOperation(value = "获取生产电子看板信息", notes = "获取生产电子看板信息", hidden = true)
	@RequestMapping(value = "/getScdzList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getScdzList(String class_nos, String dep_id, String sdata, String edata) {
		String method = "/kanban/getScdzList";
		String methodName = "获取生产电子看板信息";
		try {
			ApiResponseResult result = kanbanService.getScdzList(class_nos, dep_id, sdata, edata, this.getIpAddr());
			logger.debug("获取生产电子看板信息=getScdzList:" + result);
			getSysLogService().success(method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产电子看板信息失败！", e);
			getSysLogService().error(method, methodName, e.toString());
			return ApiResponseResult.failure("获取生产电子看板信息失败！");
		}
	}
}
