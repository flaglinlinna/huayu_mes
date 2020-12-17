package com.web.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import com.web.quote.service.QuoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "报价信息模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quote")
public class QuoteController extends WebController {

	private String module = "报价信息";

	@Autowired
	private QuoteService quoteService;

	@ApiOperation(value = "报价信息表结构", notes = "报价信息表结构" + Quote.TABLE_NAME)
	@RequestMapping(value = "/getQuote", method = RequestMethod.GET)
	@ResponseBody
	public Quote getQuote() {
		return new Quote();
	}

	@ApiOperation(value = "报价新增信息列表页", notes = "报价新增信息列表页", hidden = true)
	@RequestMapping(value = "/toQuoteAdd")
	public ModelAndView toQuoteAdd() {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult profitProdList = quoteService.getProfitProd();
			mav.addObject("profitProdList", profitProdList);
			mav.setViewName("/web/quote/01business/quote_add");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "报价信息列表页", notes = "报价信息列表页", hidden = true)
	@RequestMapping(value = "/toQuoteList")
	public String toQuoteList() {
		return "/web/quote/01business/quote_list";
	}

	@ApiOperation(value = "新增报价单", notes = "新增报价单", hidden = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult add(@RequestBody Quote quote) {
		String method = "quote/add";
		String methodName = "新增报价单";
		try {
			ApiResponseResult result = quoteService.add(quote);
			logger.debug("新增报价单=add:");
			getSysLogService().success(module, method, methodName, quote.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单新增失败！", e);
			getSysLogService().error(module, method, methodName, quote.toString() + "," + e.toString());
			return ApiResponseResult.failure("报价单新增失败！");
		}
	}
	
	@ApiOperation(value = "获取报价单列表", notes = "获取报价单列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "quote/getList";String methodName ="获取报价单列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取报价单列表=getList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价单列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取报价单列表失败！");
        }
    }
}
