package com.web.report.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.report.service.BackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "追溯报表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/back")
public class BackController extends WebController {

	private String module = "追溯报表";

	@Autowired
	private BackService backService;

	@ApiOperation(value = "追溯报表", notes = "追溯报表", hidden = true)
	@RequestMapping(value = "/toBack")
	public String toLineEffic() {
		return "/web/report/back";
	}
	
	
	@ApiOperation(value = "追溯报表", notes = "追溯报表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String dates) {
		String method = "report/line_effic/getList";
		String methodName = "追溯报表";String param = "关键字:"+keyword+",日期:"+dates;
		try {
			String[] date = {"",""};
			if(StringUtils.isNotEmpty(dates)){
				date = dates.split(" - ");
			}
			Sort sort = Sort.unsorted();
			ApiResponseResult result = backService.getList(keyword,date[0],date[1],super.getPageRequest(sort));
			logger.debug("追溯=getList:");
//			getSysLogService().success(module, method, methodName, param);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName,param+ e.toString());
			return ApiResponseResult.failure(methodName+"失败！"+e.toString());
		}
	}
}
