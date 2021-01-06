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
import com.web.report.service.WorkTimeMesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工时统计表(MES)")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/worktime_m")
public class WorkTimeMesController extends WebController {

	private String module = "工时统计表(MES)";

	@Autowired
	private WorkTimeMesService workTimeMesService;

	@ApiOperation(value = "工时统计表(MES)", notes = "工时统计表(MES)", hidden = true)
	@RequestMapping(value = "/toWorkTimeMes")
	public String toLineEffic() {
		return "/web/report/worktime_m";
	}
	
	
	 @ApiOperation(value="获取组长列表信息", notes="获取组长列表信息", hidden = true)
	    @RequestMapping(value = "/getLinerList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getLinerList(String keyword) {
	        String method = "report/worktime_m/getLinerList";String methodName ="获取组长列表信息";
	        try {
	            ApiResponseResult result = workTimeMesService.getLinerList(keyword);
	            logger.debug("获取组长列表信息=getLinerList:");
	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取组长列表信息失败！", e);
	             getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取组长列表信息失败！");
	        }
	    }
	
	 @ApiOperation(value="获取物料列表信息", notes="获取物料列表信息", hidden = true)
	    @RequestMapping(value = "/getItemList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getItemList(String keyword) {
	        String method = "report/worktime_m/getItemList";String methodName ="获取物料列表信息";
	        try {
	        	Sort sort = Sort.unsorted();
	            ApiResponseResult result = workTimeMesService.getItemList(keyword,
	            		super.getPageRequest(sort));
	            logger.debug("获取物料列表信息=getItemList:");
	            getSysLogService().success(module,method, methodName, "关键字:"+keyword+";");
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取物料列表信息失败！", e);
	             getSysLogService().error(module,method, methodName,"关键字:"+keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取物料列表信息失败！");
	        }
	    }
	 
	 
	@ApiOperation(value = "工时统计表(Mes)", notes = "工时统计表(Mes)", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String sdate,String edate,String liner_id,String itemCode) {
		String method = "report/worktime_m/getList";

		String methodName = "获取工时统计表(Mes)";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = workTimeMesService.getList(sdate,edate,liner_id,itemCode
					,super.getPageRequest(sort));
			logger.debug("工时统计表(Mes)=getList:");
			getSysLogService().success(module, method, methodName, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！"+e.toString());
		}
	}
	
	@ApiOperation(value = "工时统计表(Mes)-明细", notes = "工时统计表(Mes)-明细", hidden = true)
	@RequestMapping(value = "/getListDetail", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getListDetail(String list_id) {
		String method = "report/worktime_m/getListDetail";
		String methodName = "工时统计表(Mes)-明细";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = workTimeMesService.getListDetail(list_id,super.getPageRequest(sort));
			logger.debug("工时统计表(Mes)-明细=getListDetail:");
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
