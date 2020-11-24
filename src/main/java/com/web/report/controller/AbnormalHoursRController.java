package com.web.report.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.web.report.service.AbnormalHoursRService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工时异常统计表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/abnormal_r")
public class AbnormalHoursRController extends WebController {

	private String module = "工时异常统计表";

	@Autowired
	private AbnormalHoursRService abnormalHoursRService;

	@ApiOperation(value = "工时异常统计表", notes = "工时异常统计表", hidden = true)
	@RequestMapping(value = "/toAbnormalHoursR")
	public String toLineEffic() {
		return "/web/report/abnormal_r";
	}
	
	 @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
	    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getTaskNo(String keyword) {
	        String method = "report/abnormal_r/getTaskNo";String methodName ="获取指令单信息";
	        try {
	            ApiResponseResult result = abnormalHoursRService.getTaskNo(keyword);
	            logger.debug("获取指令单信息=getTaskNo:");
	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取指令单信息失败！", e);
	             getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取指令单信息失败！");
	        }
	    }
	 
	 @ApiOperation(value="获取组长信息", notes="获取组长信息", hidden = true)
	    @RequestMapping(value = "/getLiner", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getLiner(String keyword) {
	        String method = "report/abnormal_r/getLiner";String methodName ="获取组长信息";
	        try {
	            ApiResponseResult result = abnormalHoursRService.getLiner(keyword);
	            logger.debug("获取组长信息=getLiner:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取组长信息失败！", e);
	             getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取组长信息失败！");
	        }
	    }
	
	 @ApiOperation(value="获取员工信息", notes="获取员工信息", hidden = true)
	    @RequestMapping(value = "/getEmpCode", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getEmpCode(String keyword) {
	        String method = "report/abnormal_r/getEmpCode";String methodName ="获取员工信息";
	        try {
	            ApiResponseResult result = abnormalHoursRService.getEmpCode(keyword);
	            logger.debug("获取员工信息=getEmpCode:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取员工信息失败！", e);
	             getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取员工信息失败！");
	        }
	    }
	
	
	@ApiOperation(value = "工时异常统计表", notes = "工时异常统计表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String sdate,String edate,String empCode,String liner,String taskno) {
		String method = "report/abnormal_r/getList";
//		@RequestBody Map<String, Object> params
//		String sdate=params.get("sdate").toString();
//		String edate=params.get("edate").toString();
//		String empCode=params.get("empCode").toString();
//		String liner=params.get("liner").toString();
//		String taskno=params.get("taskno").toString();
		
		String methodName = "工时异常统计表";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = abnormalHoursRService.getList(sdate,edate,liner,empCode,taskno
					,super.getPageRequest(sort));
			logger.debug("追溯=getList:");
//			getSysLogService().success(module, method, methodName, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！"+e.toString());
		}
	}
}
