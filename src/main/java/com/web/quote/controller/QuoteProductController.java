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
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.service.QuoteProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "报价制造信息模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteProdect")
public class QuoteProductController extends WebController {

	private String module = "报价制造信息模块";

	@Autowired
	private QuoteProductService quoteProductService;

	@ApiOperation(value = "报价新增信息列表页", notes = "报价新增信息列表页", hidden = true)
	@RequestMapping(value = "/toQuoteProdect")
	public ModelAndView toQuoteProdect() {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("/web/quote/02produce/product_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "获取报价单列表", notes = "获取报价单列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "quote/getList";String methodName ="获取报价单列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteProductService.getList(keyword, super.getPageRequest(sort));
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
