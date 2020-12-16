package com.web.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping(value = "price/quote")
public class QuoteController extends WebController {
	
	private String module = "报价信息";
	
	 @Autowired
	 private QuoteService quoteService;
	 
	 @ApiOperation(value = "报价信息表结构", notes = "报价信息表结构"+Quote.TABLE_NAME)
	    @RequestMapping(value = "/getQuote", method = RequestMethod.GET)
		@ResponseBody
	    public Quote getQuote(){
	        return new Quote();
	    }
	 
	 @ApiOperation(value = "报价信息列表页", notes = "报价信息列表页", hidden = true)
	    @RequestMapping(value = "/toQuote")
	    public String toQuote(){
	        return "/price/quote/quote";
	    }
	 
	 @ApiOperation(value = "新增报价单", notes = "新增报价单", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Quote quote) {
	        String method = "price/quote/add";String methodName ="新增报价单";
	        try{
	            ApiResponseResult result = quoteService.add(quote);
	            logger.debug("新增报价单=add:");
	            getSysLogService().success(module,method, methodName, quote.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("报价单新增失败！", e);
	            getSysLogService().error(module,method, methodName, quote.toString()+","+e.toString());
	            return ApiResponseResult.failure("报价单新增失败！");
	        }
	    }
}
