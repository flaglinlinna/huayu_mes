package com.web.kanban.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
		mav.setViewName("/kanban/index1");// 返回路径
		return mav;
	}
	
	//（复数看板）轮播页面
	@RequestMapping(value = "/toCjkbs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjkbs(String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjkbs";
		String methodName = "车间看板【车间报工+生产电子】";
		try {	
			ApiResponseResult rotation =kanbanService.getRotationTime();
			mav.addObject("rotation",rotation);
			mav.addObject("inType",inType);
			mav.setViewName("/kanban/cjkbs");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("车间看板【车间报工+生产电子】异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}

		@RequestMapping(value = "/toLtkbs", method = RequestMethod.GET)
		@ResponseBody
		public ModelAndView toLtkbs(String liner,String inType) {
			ModelAndView mav = new ModelAndView();
			String method = "/kanban/toLtkbs";
			String methodName = "获取拉头看板";
			try {	
				ApiResponseResult rotation =kanbanService.getRotationTime();
				mav.addObject("rotation",rotation);
				mav.addObject("liner",liner);
				mav.addObject("inType",inType);
				mav.setViewName("/kanban/ltkbs");// 返回路径
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("获取拉头看板异常！", e);
				getSysLogService().error(module,method,methodName,e.toString());
			}
			return mav;
		}
	
		@RequestMapping(value = "/toSetLiner", method = RequestMethod.GET)
		@ResponseBody
		public ModelAndView toSetLiner(String inType,String pageType) {
			ModelAndView mav = new ModelAndView();
			String method = "/kanban/toSetLiner";
			String methodName = "获取组长数据-拉头看板";
			try {	
				ApiResponseResult linerList=kanbanService.getLiner();
				mav.addObject("linerList",linerList);
				mav.addObject("inType",inType);
				mav.addObject("pageType",pageType);
				mav.setViewName("/kanban/setliner");// 返回路径
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("获取组长数据-拉头看板异常！", e);
				getSysLogService().error(module,method,methodName,e.toString());
			}
			return mav;
		}
		
		
	//不带参数的默认获取

	@RequestMapping(value = "/toCjbg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbg() {
		String method = "/kanban/toCjbg";
		String methodName = "车间报工看板";
		ModelAndView mav = new ModelAndView();
		// mav.addObject("pname", p);
		mav.setViewName("/kanban/index1");// 返回路径
		return mav;
	}

	@RequestMapping(value = "/toCjbg1", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbg1(String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjbg1";
		String methodName = "车间报工看板";
		try {	
			ApiResponseResult result = kanbanService.getCjbgList("999","","",this.getIpAddr());
			ApiResponseResult deptList=kanbanService.getCjbgDepList();
			ApiResponseResult interval =kanbanService.getIntervalTime();
			logger.debug("获取看板=toCjbg1:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList",deptList);
			mav.addObject("interval",interval);
			mav.addObject("inType",inType);
			mav.setViewName("/kanban/cjbg1");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toCjbgDetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbgDetail(String liner) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjbgDetail";
		String methodName = "车间报工看板数据穿透";
		try {
			ApiResponseResult result = kanbanService.getCjbgDetailList(liner, this.getIpAddr());
			logger.debug(methodName+"=toCjbgDetail:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("liner", liner);
			mav.setViewName("/kanban/cjbg_detail");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toScdz", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toScdz(String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toScdz";
		String methodName = "生产电子看板";
		try {	
			ApiResponseResult result = kanbanService.getScdzList("999","","",this.getIpAddr());
			ApiResponseResult deptList = kanbanService.getCjbgDepList();
			ApiResponseResult interval =kanbanService.getIntervalTime();
			logger.debug("获取生产电子看板=toScdz:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("interval",interval);
			mav.addObject("inType",inType);
			mav.setViewName("/kanban/scdz");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产电子看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toScdzDetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toScdzDetail(String liner,String fieldword) {//数据穿透sjct1
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toScdzDetail";
		String methodName = "生产电子看板数据穿透";
		try {
			ApiResponseResult result = kanbanService.getScdzDetailList(liner, "",this.getIpAddr(),fieldword);
			logger.debug(methodName+"=toScdzDetail:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("liner", liner);
			mav.addObject("fieldword", fieldword);
			mav.setViewName("/kanban/scdz_detail");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toZcbl", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toZcbl(String line,String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toZcbl";
		String methodName = "制程不良看板";
		try {	
			ApiResponseResult result = kanbanService.getZcblList("999","","",this.getIpAddr());
			ApiResponseResult deptList=kanbanService.getZcblDepList();
			ApiResponseResult interval =kanbanService.getIntervalTime();
			logger.debug("制程不良看板=toZcbl:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList",deptList);
			mav.addObject("interval",interval);
			mav.addObject("inType",inType);
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
	public ModelAndView toXlpm(String liner,String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toXlpm";
		String methodName = "效率排名看板";
		try {	
			ApiResponseResult result = kanbanService.getXlpmList("999","","",this.getIpAddr(),liner);
			ApiResponseResult deptList=kanbanService.getCjbgDepList();
			ApiResponseResult linerList=kanbanService.getLiner();
			ApiResponseResult interval =kanbanService.getIntervalTime();
			logger.debug("效率排名看板=toXlpm:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("linerList", linerList);
			mav.addObject("nowLiner", liner);
			mav.addObject("interval",interval);
			mav.addObject("inType",inType);
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
	public ModelAndView toDfg(String inType) {
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
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ApiResponseResult result = kanbanService.getDfgList("1","",formatter.format(date),usr_id,"1");//this.getIpAddr()
			ApiResponseResult deptList=kanbanService.getZcblDepList();
			ApiResponseResult interval =kanbanService.getIntervalTime();
			logger.debug("待返工看板=toDfg:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("interval",interval);
			mav.addObject("inType",inType);
			mav.setViewName("/kanban/dfg");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取待返工看板看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toCxdz", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCxdz(String liner,String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toCxdz";
		String methodName = "产线电子看板";
		try {	
			ApiResponseResult result = kanbanService.getCxdzList("","","",this.getIpAddr(),liner);//this.getIpAddr()
			ApiResponseResult deptList=kanbanService.getCjbgDepList();
			ApiResponseResult linerList=kanbanService.getLiner();
			ApiResponseResult interval =kanbanService.getIntervalTime();
			logger.debug("产线电子看板=toCxdz:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("linerList", linerList);
			mav.addObject("interval",interval);
			mav.addObject("nowLiner", liner);
			mav.addObject("inType",inType);
			mav.setViewName("/kanban/cxdz");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线电子看板异常！", e);
			getSysLogService().error(module,method,methodName,e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toCxsc", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCxsc(String liner,String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toCxsc";
		String methodName = "产线生产看板";
		try {	
			ApiResponseResult result = kanbanService.getCxscList("","",liner,this.getIpAddr(),"1");//this.getIpAddr()
			ApiResponseResult deptList=kanbanService.getCjbgDepList();
			ApiResponseResult linerList=kanbanService.getLiner();
			ApiResponseResult interval =kanbanService.getIntervalTime();
			logger.debug("产线生产看板=toCxsc:" + result);
			getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("linerList", linerList);
			mav.addObject("interval",interval);
			mav.addObject("nowLiner", liner);
			mav.addObject("inType",inType);
			mav.setViewName("/kanban/cxsc");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线生产看板异常！", e);
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
	@ApiOperation(value = "获取车间报工看板数据穿透信息", notes = "获取车间报工看板数据穿透信息", hidden = true)
	@RequestMapping(value = "/getCjbgDetailList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCjbgDetailList(String liner) {
		String method = "/kanban/getCjbgDetailList";
		String methodName = "获取车间报工看板数据穿透信息";
		try {
			ApiResponseResult result = kanbanService.getCjbgDetailList(liner, this.getIpAddr());
			logger.debug(methodName+"getCjbgDetailList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}
	
	//带参数的二次获取

	@ApiOperation(value = "获取制程不良看板信息", notes = "获取制程不良看板信息", hidden = true)
	@RequestMapping(value = "/getZcblList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getZcblList(String class_nos, String dep_id, String sdata) {
		String method = "/kanban/getZcblList";
		String methodName = "获取制程不良看板信息";
		try {
			ApiResponseResult result = kanbanService.getZcblList(class_nos, dep_id, sdata, this.getIpAddr());
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
	@ApiOperation(value = "获取生产电子看板数据穿透信息", notes = "获取生产电子看板数据穿透信息", hidden = true)
	@RequestMapping(value = "/getScdzDetailList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getScdzDetailList(String liner, String dep_id,String fieldword) {//sjct2 数据穿透
		String method = "/kanban/getScdzDetailList";
		String methodName = "获取生产电子看板数据穿透信息";
		try {
			ApiResponseResult result = kanbanService.getScdzDetailList(liner, dep_id,this.getIpAddr(),fieldword);
			logger.debug(methodName+"=getScdzDetailList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
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
	/**
	 * 获取产线电子看板
	 * */	
	@ApiOperation(value = "获取产线电子看板信息", notes = "获取产线电子看板信息", hidden = true)
	@RequestMapping(value = "/getCxdzList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCxdzList(String class_nos, String dep_id, String sdata ,String liner) {
		String method = "/kanban/getCxdzList";
		String methodName = "获取产线电子看板信息";
		try {
			ApiResponseResult result = kanbanService.getCxdzList(class_nos, dep_id, sdata,this.getIpAddr(),liner);
			logger.debug("获取产线电子看板信息=getCxdzList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线电子看板信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取产线电子看板信息失败！");
		}
	}
	/**
	 * 获取产线生产看板
	 * */	
	@ApiOperation(value = "获取产线生产看板信息", notes = "获取产线生产看板信息", hidden = true)
	@RequestMapping(value = "/getCxscList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCxscList(String taskNo,String deptId,String liner,String interval) {
		String method = "/kanban/getCxscList";
		String methodName = "获取产线生产看板信息";
		try {
			ApiResponseResult result = kanbanService.getCxscList(taskNo, deptId, liner,this.getIpAddr(),interval);
			logger.debug("获取产线生产看板信息=getCxscList:" + result);
			getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线生产看板信息失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("获取产线生产看板信息失败！");
		}
	}
}
