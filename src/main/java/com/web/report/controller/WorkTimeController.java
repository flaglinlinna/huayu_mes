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
import com.web.report.service.WorkTimeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工时统计表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/worktime")
public class WorkTimeController extends WebController {

	private String module = "工时统计表";

	@Autowired
	private WorkTimeService workTimeService;

	@ApiOperation(value = "工时统计表", notes = "工时统计表", hidden = true)
	@RequestMapping(value = "/toWorkTime")
	public String toLineEffic() {
		return "/web/report/worktime";
	}
	
	
	 @ApiOperation(value="获取员工信息", notes="获取员工信息", hidden = true)
	    @RequestMapping(value = "/getEmpCode", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getEmpCode(String keyword) {
	        String method = "report/worktime/getEmpCode";String methodName ="获取员工信息";
	        try {
	            ApiResponseResult result = workTimeService.getEmpCode(keyword);
	            logger.debug("获取员工信息=getEmpCode:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取员工信息失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
//	             getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取员工信息失败！");
	        }
	    }
	
	@ApiOperation(value = "工时统计表", notes = "工时统计表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String sdate,String edate,String empCode,String line_id) {
		String method = "report/worktime/getList";
		String param = "开始日期:"+sdate +"结束日期："+edate +"员工编号:"+empCode +"线别id："+ line_id;
		String methodName = "获取工时统计表";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = workTimeService.getList(sdate,edate,line_id,empCode
					,super.getPageRequest(sort));
			logger.debug("工时统计表=getList:");
//			getSysLogService().success(module, method, methodName, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName,param+ e.toString());
			return ApiResponseResult.failure(methodName+"失败！"+e.toString());
		}
	}
	
	@ApiOperation(value = "工时统计表-明细", notes = "工时统计表-明细", hidden = true)
	@RequestMapping(value = "/getListDetail", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getListDetail(@RequestBody Map<String, Object> param) {
		String method = "report/worktime/getListDetail";
		String list_id=param.get("list_id").toString();
		String methodName = "工时统计表-明细";
		try {
			ApiResponseResult result = workTimeService.getListDetail(list_id);
			logger.debug("工时统计表-明细=getListDetail:");
//			getSysLogService().success(module, method, methodName, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName,param+ e.toString());
			return ApiResponseResult.failure(methodName+"失败！"+e.toString());
		}
	}
}
