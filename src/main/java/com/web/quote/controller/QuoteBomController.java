package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteBom;
import com.web.quote.service.QuoteBomService;
import com.web.quote.service.QuoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Api(description = "报价信息模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteBom")
public class QuoteBomController extends WebController {

	private String module = "报价信息";

	@Autowired
	private QuoteBomService quoteBomService;

	
	@ApiOperation(value = "报价项目-Bom", notes = "报价项目-Bom", hidden = true)
	@RequestMapping(value = "/toQuoteBom")
	public ModelAndView toQuoteBom(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/01business/quote_bom");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}


	@ApiOperation(value = "获取报价BOM清单列表", notes = "获取报价BOM清单列表",hidden = true)
	@RequestMapping(value = "/getQuoteBomList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getQuoteBomList(String keyword,Long pkQuote) {
		String method = "quote/getQuoteBomList";String methodName ="获取报价BOM清单列表";
		try {
			Sort sort = new Sort(Sort.Direction.ASC, "id");
			ApiResponseResult result = quoteBomService.getQuoteBomList(keyword,pkQuote, super.getPageRequest(sort));
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
		String method = "/QuoteBom/importExcel";String methodName ="导入模板";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return quoteBomService.doQuoteBomExcel(file,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}

	@ApiOperation(value = "删除外购件信息", notes = "删除外购件信息", hidden = true)
	@RequestMapping(value = "/deleteQuoteBom", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "quote/deleteQuoteBom";
		String methodName = "删除外购件信息";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = quoteBomService.deleteQuoteBom(id);
			logger.debug("删除外购件信息=delete:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除外购件信息失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("删除外购件信息失败！");
		}
	}
}
