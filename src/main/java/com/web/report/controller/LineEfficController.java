package com.web.report.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.report.service.LineEfficService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "各线效率明细报表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/line_effic")
public class LineEfficController extends WebController {

	private String module = "各线效率明细报表";

	@Autowired
	private LineEfficService lineEfficService;

	@ApiOperation(value = "各线效率明细报表", notes = "各线效率明细报表", hidden = true)
	@RequestMapping(value = "/toLineEffic")
	public String toLineEffic() {
		return "/web/report/line_effic";
	}
	
	
	@ApiOperation(value = "获取各线效率明细报表", notes = "获取各线效率明细报表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String dates) {
		String method = "report/line_effic/getList";
		String methodName = "获取各线效率明细报表";String param = "关键字:"+keyword+",日期:"+dates;
		try {
			String[] date = {"",""};
			if(StringUtils.isNotEmpty(dates)){
				date = dates.split(" - ");
			}
			ApiResponseResult result = lineEfficService.getList(keyword,date[0],date[1]);
			logger.debug("获取获取检验批次报表(FQC)=getCheckBatchReport:");
//			getSysLogService().success(module, method, methodName, param);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, param+e.toString());
			return ApiResponseResult.failure(methodName+"失败！"+e.toString());
		}
	}
}
