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
import com.web.report.service.MesHrDifferService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "人数差异统计表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/differ")
public class MesHrDifferController extends WebController {

	private String module = "人数差异统计表";

	@Autowired
	private MesHrDifferService mesHrDifferService;

	@ApiOperation(value = "人数差异统计表", notes = "人数差异统计表", hidden = true)
	@RequestMapping(value = "/toMesHrDiffer")
	public String toLineEffic() {
		return "/web/report/differ";
	}
	
	
	 @ApiOperation(value="获取员工信息", notes="获取员工信息", hidden = true)
	    @RequestMapping(value = "/getEmpCode", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getEmpCode(String keyword) {
	        String method = "report/differ/getLiner";String methodName ="获取员工信息";
	        try {
	            ApiResponseResult result = mesHrDifferService.getEmpCode(keyword);
	            logger.debug("获取员工信息=getEmpCode:");
	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取员工信息失败！", e);
	             getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取员工信息失败！");
	        }
	    }
	
	
	@ApiOperation(value = "人数差异统计表", notes = "人数差异统计表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getList(@RequestBody Map<String, Object> params) {
		String method = "report/differ/getList";
		
		String sdate=params.get("sdate").toString();
		String edate=params.get("edate").toString();
		String empCode=params.get("empCode").toString();		
		String methodName = "获取人数差异统计表";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = mesHrDifferService.getList(sdate,edate,empCode
					,super.getPageRequest(sort));
			logger.debug("人数差异统计表=getList:");
			getSysLogService().success(module, method, methodName, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！"+e.toString());
		}
	}
}
