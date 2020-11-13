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
import com.system.user.entity.SysUser;
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

	private String module = "看板信息";

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
			//参数 String class_no, String dep_id, String sdata, String edata,String dev_ip
			ApiResponseResult result = kanbanService.getCjbgList("999",line,"",this.getIpAddr());
			logger.debug("获取看板=toCjbg1:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("dev_ip", this.getIpAddr());
			mav.setViewName("/kanban/cjbg1");// 返回路径
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
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
			ApiResponseResult result = kanbanService.getScdzList("999","","",this.getIpAddr());
			logger.debug("获取生产电子看板=toScdz:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("dev_ip", this.getIpAddr());
			mav.setViewName("/kanban/scdz");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产电子看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
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
			ApiResponseResult result = kanbanService.getZcblList("999",line,"",this.getIpAddr());
			logger.debug("制程不良看板=toZcbl:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("dev_ip", this.getIpAddr());
			mav.setViewName("/kanban/zcbl");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制程不良看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toXlpm", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toXlpm(String line,String liner) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toXlpm";
		String methodName = "效率排名看板";
		try {	
			ApiResponseResult result = kanbanService.getXlpmList("999",line,"",this.getIpAddr(),liner);
			logger.debug("效率排名看板=toXlpm:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("dev_ip", this.getIpAddr());
			mav.setViewName("/kanban/xlpm");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取效率排名看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toDfg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toDfg() {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toDfg";
		String methodName = "待返工看板";
		try {	
			String usr_id="";
			SysUser currUser = UserUtil.getSessionUser();
            if(currUser == null){
            	usr_id="1";
            }else{
            	usr_id=currUser.getId().toString();
            }
			ApiResponseResult result = kanbanService.getDfgList("1","5252","2020-11-10",usr_id,"1");//this.getIpAddr()
			logger.debug("待返工看板=toDfg:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("dev_ip", this.getIpAddr());
			mav.setViewName("/kanban/dfg");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取待返工看板看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	

	//--------------getList-------------------
	
	@ApiOperation(value = "获取车间报工看板信息", notes = "获取车间报工看板信息", hidden = true)
	@RequestMapping(value = "/getCjbgList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCjbgList(String class_nos, String dep_id, String sdata) {
		String method = "/kanban/getCjbgList";
		String methodName = "获取车间报工看板信息";
		try {
			ApiResponseResult result = kanbanService.getCjbgList(class_nos, dep_id, sdata, this.getIpAddr());
			logger.debug("获取车间报工看板信息=getCjbgList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取车间报工看板信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取车间报工看板信息失败！");
		}
	}
	
	

	@ApiOperation(value = "获取制程不良看板信息", notes = "获取制程不良看板信息", hidden = true)
	@RequestMapping(value = "/getZcblList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getZcblList(String class_no, String dep_id, String sdata) {
		String method = "/kanban/getZcblList";
		String methodName = "获取制程不良看板信息";
		try {
			ApiResponseResult result = kanbanService.getZcblList(class_no, dep_id, sdata, this.getIpAddr());
			logger.debug("获取制程不良看板信息=getZcblList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制程不良看板信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取制程不良看板信息失败！");
		}
	}

	@ApiOperation(value = "获取生产电子看板信息", notes = "获取生产电子看板信息", hidden = true)
	@RequestMapping(value = "/getScdzList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getScdzList(String class_nos, String dep_id, String sdata) {
		String method = "/kanban/getScdzList";
		String methodName = "获取生产电子看板信息";
		try {
			ApiResponseResult result = kanbanService.getScdzList(class_nos, dep_id, sdata,  this.getIpAddr());
			logger.debug("获取生产电子看板信息=getScdzList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产电子看板信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取生产电子看板信息失败！");
		}
	}
	
	@ApiOperation(value = "获取效率排名看板信息", notes = "获取效率排名看板信息", hidden = true)
	@RequestMapping(value = "/getXlpmList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getXlpmList(String class_nos, String dep_id, String sdata ,String liner) {
		String method = "/kanban/getXlpmList";
		String methodName = "获取效率排名看板信息";
		try {
			ApiResponseResult result = kanbanService.getXlpmList(class_nos, dep_id, sdata,  this.getIpAddr(),liner);
			logger.debug("获取效率排名看板信息=getXlpmList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取效率排名看板信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取效率排名看板信息失败！");
		}
	}
	
	@ApiOperation(value = "获取待返工看板信息", notes = "获取返工看板信息", hidden = true)
	@RequestMapping(value = "/getDfgList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getDfgList(String class_id,String dep_id, String sdata) {
		String method = "/kanban/getDfgList";
		String methodName = "获取返工看板信息";
		try {
			String usr_id="";
			SysUser currUser = UserUtil.getSessionUser();
            if(currUser == null){
            	usr_id="1";
            }else{
            	usr_id=currUser.getId().toString();
            }
			ApiResponseResult result = kanbanService.getDfgList(class_id,dep_id, sdata, usr_id, "1");
			logger.debug("获取返工看板信息=getDfgList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取返工看板信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取返工看板信息失败！");
		}
	}
	
	/*
	 * 获取部门信息
	 * */

	@ApiOperation(value = "获取部门信息", notes = "获取部门信息", hidden = true)
	@RequestMapping(value = "/getCjbgDepList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCjbgDepList(String class_nos, String dep_id, String sdata, String edata) {
		String method = "/kanban/getCjbgDepList";
		String methodName = "获取部门信息";
		try {
			ApiResponseResult result = kanbanService.getCjbgDepList();
			logger.debug("获取部门信息=getCjbgList:" + result);
			
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取部门信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取部门信息失败！");
		}
	}
	
	/*
	 * 获取部门信息--制程不良
	 * */

	@ApiOperation(value = "获取部门信息-制程不良", notes = "获取部门信息-制程不良", hidden = true)
	@RequestMapping(value = "/getZcblDepList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getZcblDepList() {
		String method = "/kanban/getZcblDepList";
		String methodName = "获取部门信息-制程不良";
		try {
			ApiResponseResult result = kanbanService.getZcblDepList();
			logger.debug("获取部门信息-制程不良=getZcblDepList:" + result);
			
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取部门信息-制程不良失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取部门信息-制程不良失败！");
		}
	}
	
	/*
	 * 取线长信息
	 * */
	@ApiOperation(value = "获取组长信息", notes = "获取组长信息", hidden = true)
	@RequestMapping(value = "/getLiner", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getLiner() {
		String method = "/kanban/getLiner";
		String methodName = "获取组长信息";
		try {
			ApiResponseResult result = kanbanService.getLiner();
			logger.debug("获取组长信息=getLiner:" + result);
			
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取组长信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取组长失败！");
		}
	}
}
