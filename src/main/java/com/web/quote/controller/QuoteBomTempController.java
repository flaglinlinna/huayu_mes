package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.service.QuoteBomTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Api(description = "外购件清单临时表模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteBomTemp")
public class QuoteBomTempController extends WebController {

	private String module = "外购件清单导入临时表信息";

	@Autowired
	private QuoteBomTempService quoteBomTempService;


	@ApiOperation(value = "获取报价BOM清单列表", notes = "获取报价BOM清单列表",hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String quoteId) {
		String method = "quoteBomTemp/getList";String methodName ="获取报价BOM清单列表";
		try {
			Sort sort = new Sort(Sort.Direction.ASC, "id");
			ApiResponseResult result = quoteBomTempService.getList(keyword,quoteId, super.getPageRequest(sort));
			logger.debug("获取报价BOM清单列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价BOM清单列表失败！", e);
			getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
			return ApiResponseResult.failure("获取报价BOM清单列表失败！");
		}
	}

	@ApiOperation(value="导入模板", notes="导入模板", hidden = true)
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getExcel(MultipartFile[] file, Long pkQuote ) {
		String method = "/quoteBomTemp/importExcel";String methodName ="导入模板到临时表";
		try {
			logger.debug("导入模板到临时表=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return quoteBomTempService.doExcel(file,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入模板到临时表失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}


	@ApiOperation(value="确定导入正式表", notes="确定导入正式表", hidden = true)
	@RequestMapping(value = "/uploadChecked", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult uploadChecked(@RequestBody Map<String, Object> params) {
		String method = "/quoteBomTemp/uploadChecked";String methodName ="确定导入正式表";
		try {
			Long pkQuote = Long.parseLong(params.get("pkQuote").toString());
			logger.debug("确定导入正式表=uploadCheck:");
			getSysLogService().success(module,method, methodName, "");
			return  quoteBomTempService.importByTemp(pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("确定导入正式表失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("确定导入失败！");
		}
	}

	@ApiOperation(value="批量删除选中的临时导入数据", notes="批量删除选中的临时导入数据", hidden = true)
	@RequestMapping(value = "/deleteTemp", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult deleteTemp(@RequestBody Map<String, Object> params) {
		String method = "/quoteBomTemp/deleteTemp";String methodName ="批量删除选中的临时导入数据";
		try {
			String ids = params.get("ids").toString();
			logger.debug("确定导入正式表=uploadCheck:");
			getSysLogService().success(module,method, methodName, "");
			return  quoteBomTempService.deleteTemp(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("确定导入正式表失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("删除失败！");
		}
	}
}
