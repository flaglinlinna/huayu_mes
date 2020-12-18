package com.web.quote.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.QuoteProcessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "报价工艺流程模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteProcess")
public class QuoteProcessController extends WebController {

	private String module = "报价工艺流程";

	@Autowired
	private QuoteProcessService quoteProcessService;

	@ApiOperation(value = "报价工艺流程表结构", notes = "报价工艺流程表结构" + QuoteProcess.TABLE_NAME)
	@RequestMapping(value = "/getQuoteProcess", method = RequestMethod.GET)
	@ResponseBody
	public QuoteProcess getQuoteProcess() {
		return new QuoteProcess();
	}

	
	@ApiOperation(value = "报价工艺流程页", notes = "报价工艺流程页", hidden = true)
	@RequestMapping(value = "/toQuoteProcess")
	public ModelAndView toQuoteProcess() {
		ModelAndView mav = new ModelAndView();
		try {
			//mav.addObject("info", info);
			mav.setViewName("/web/quote/01business/quote_process");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程页数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "quoteProcess/getList";String methodName ="获取报价工艺流程列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteProcessService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取报价工艺流程列表=getList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价工艺流程列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取报价工艺流程列表失败！");
        }
    }	
}
