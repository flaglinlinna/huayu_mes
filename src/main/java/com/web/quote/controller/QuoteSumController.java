package com.web.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.service.QuoteSumService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.Map;

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
	public ModelAndView toQuoteSumList(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/04summary/quote_sum_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单汇总页失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "报价单汇详情页", notes = "报价单汇详情页", hidden = true)
	@RequestMapping(value = "/toQuoteSum")
	public ModelAndView toQuoteSum(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult quoteDetail=quoteSumService.getSumByQuote(quoteId);
			mav.addObject("quoteId", quoteId);
			mav.addObject("quoteDetail", quoteDetail);
			mav.setViewName("/web/quote/04summary/quote_sum");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单汇详情页失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "报价BOM树形", notes = "报价BOM树形", hidden = true)
	@RequestMapping(value = "/toQuoteTree")
	public ModelAndView toQuoteTree(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult quoteBom=quoteSumService.getQuoteBomByQuote(quoteId);
			mav.addObject("quoteId", quoteId);
			mav.addObject("permList", quoteBom.getData());
			mav.setViewName("/web/quote/04summary/quote_sum_tree");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单汇详情页失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "获取报价单列表", notes = "获取报价单列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String quoteId,String keyword,String bsStatus,String bsCode,String bsType,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsProdType,String bsSimilarProd,
									 String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName) {
		String method = "/quoteSum/getList";
		String methodName = "获取报价单列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = quoteSumService.getList(quoteId,keyword, bsStatus,bsCode,bsType,bsFinishTime,bsRemarks,
					bsProd,bsProdType,bsSimilarProd,bsPosition,bsCustRequire,bsLevel,bsRequire,bsDevType,bsCustName,super.getPageRequest(sort));
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
	@ApiOperation(value = "获取报价单汇总详情-树形", notes = "获取报价单汇总详情-树形", hidden = true)
	@RequestMapping(value = "/getQuoteTreeList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getQuoteTreeList(String keyword,String quoteId) {
		String method = "/quoteSum/getQuoteTreeList";
		String methodName = "获取报价单汇总详情-树形";
		try {
			ApiResponseResult result = quoteSumService.getQuoteBomByQuote(quoteId);
			logger.debug(methodName+"=getQuoteTreeList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "修改净利润", notes = "修改净利润", hidden = true)
	@RequestMapping(value = "/updateProfitNet", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult updateProfitNet(@RequestBody Map<String, Object> params) {
		long pkQuote = Long.parseLong(params.get("pkQuote").toString()) ;
		BigDecimal profitNet = new BigDecimal(params.get("profitNet").toString());
		String method = "/quoteSum/updateProfitNet";
		String methodName = "修改净利润";
		try {
			ApiResponseResult result = quoteSumService.updateProfitNet(pkQuote,profitNet);
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}



}
