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
import com.web.quote.service.QuoteService;

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
	@Autowired
	private QuoteService quoteService;

	@ApiOperation(value = "报价信息列表页", notes = "报价信息列表页", hidden = true)
	@RequestMapping(value = "/toQuoteProdect")
	public ModelAndView toQuoteProdect(String style,String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.addObject("Style", style);
			mav.setViewName("/web/quote/02produce/product_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "填报信息项目列表页", notes = "填报信息项目列表页", hidden = true)
	@RequestMapping(value = "/toProductItem")
	public ModelAndView toQuoteItem(String quoteId,String style) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult info = quoteService.getSingle( Long.parseLong(quoteId));
			ApiResponseResult ItemList = quoteProductService.getItemPage(Long.parseLong(quoteId),style);
			mav.addObject("ItemList", ItemList);
			mav.addObject("info", info);
			mav.addObject("Style", style);
			mav.setViewName("/web/quote/02produce/product_items");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价信息项目列表页数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "获取报价单列表", notes = "获取报价单列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword,String style,String status,String bsCode,String bsType,String bsStatus,
			String bsFinishTime,String bsRemarks,String bsProd,String bsSimilarProd,
			String bsPosition ,String bsCustRequire,String bsLevel,String bsRequire,
			String bsDevType,String bsCustName,String quoteId) {
        String method = "quote/getList";String methodName ="获取报价单列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteProductService.getList(keyword,style,status,bsCode,bsType,bsStatus,bsFinishTime,
					bsRemarks,bsProd,bsSimilarProd,bsPosition,bsCustRequire,bsLevel,bsRequire,bsDevType,bsCustName,quoteId,super.getPageRequest(sort));
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
	
	@ApiOperation(value = "发起审批前校验", notes = "发起审批前校验", hidden = true)
	@RequestMapping(value = "/doCheckBefore", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult doCheckBefore(String keyword,String quoteId,String bsType) {
		String method = "/quoteProdect/doCheckBefore";
		String methodName = "采购部发起审批前校验";
		try {
			ApiResponseResult result = quoteProductService.doCheckBefore(keyword,quoteId,bsType);
			logger.debug(methodName+"=doCheckBefore:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

}
