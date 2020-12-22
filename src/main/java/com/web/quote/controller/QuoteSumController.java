package com.web.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.service.QuoteSumService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "报价单汇总模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteSum")
public class QuoteSumController extends WebController {

	private String module = "报价单汇总模块";

	@Autowired
	private QuoteSumService quoteSumService;
	
	@ApiOperation(value = "报价单汇总页", notes = "报价单汇总页", hidden = true)
	@RequestMapping(value = "/toQuoteSumList")
	public ModelAndView toQuoteSumList() {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("/web/quote/04summary/quote_sum_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单汇总页失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "报价单汇详情页", notes = "报价单汇详情页", hidden = true)
	@RequestMapping(value = "/toPurchaseEdite")
	public ModelAndView toPurchaseEdite(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/03purchase/purchase_edite");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单汇详情页失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "获取报价单列表", notes = "获取报价单列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword) {
		String method = "/quoteSum/getList";
		String methodName = "获取报价单列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = quoteSumService.getList(keyword, super.getPageRequest(sort));
			logger.debug(methodName+"=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "获取报价单汇总详情", notes = "获取报价单汇总详情", hidden = true)
	@RequestMapping(value = "/getQuoteList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getQuoteList(String keyword,String quoteId) {
		String method = "/quoteSum/getQuoteList";
		String methodName = "获取报价单汇总详情";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = quoteSumService.getQuoteList(keyword,quoteId, super.getPageRequest(sort));
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}



}
