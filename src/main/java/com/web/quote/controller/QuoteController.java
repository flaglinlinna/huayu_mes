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
import com.web.basic.service.SysParamSubService;
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
	@Autowired
	private SysParamSubService sysParamSubService;

	@ApiOperation(value = "报价信息表结构", notes = "报价信息表结构" + Quote.TABLE_NAME)
	@RequestMapping(value = "/getQuote", method = RequestMethod.GET)
	@ResponseBody
	public Quote getQuote() {
		return new Quote();
	}

	@ApiOperation(value = "报价新增信息列表页", notes = "报价新增信息列表页", hidden = true)
	@RequestMapping(value = "/toQuoteAdd")
	public ModelAndView toQuoteAdd(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult profitProdList = quoteService.getProfitProd();
			mav.addObject("quoteId", quoteId);
			mav.addObject("profitProdList", profitProdList);
			mav.addObject("Jitai", sysParamSubService.getListByMCode("BJ_BASE_MACHINE_TYPE").getData());//机台类型
			mav.setViewName("/web/quote/01business/quote_add");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "报价信息列表页", notes = "报价信息列表页", hidden = true)
	@RequestMapping(value = "/toQuoteList")
	public ModelAndView toQuoteList(String step) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("Step", step);
			mav.setViewName("/web/quote/01business/quote_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "报价信息项目列表页", notes = "报价信息项目列表页", hidden = true)
	@RequestMapping(value = "/toQuoteItem")
	public ModelAndView toQuoteItem(String quoteId,String style) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult info = quoteService.getSingle( Long.parseLong(quoteId));
			ApiResponseResult ItemList = quoteService.getItemPage( Long.parseLong(quoteId),style);
			mav.addObject("ItemList", ItemList);
			mav.addObject("info", info);
			mav.setViewName("/web/quote/01business/quote_items");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价信息项目列表页数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "报价项目-Bom", notes = "报价项目-Bom", hidden = true)
	@RequestMapping(value = "/toQuoteBom")
	public ModelAndView toQuoteBom(String quoteId,String code) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.addObject("code", code);
			mav.setViewName("/web/quote/01business/quote_bom");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
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
    public ApiResponseResult getList(String keyword,String status) {
        String method = "quote/getList";String methodName ="获取报价单列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteService.getList(keyword, status, super.getPageRequest(sort));
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
	
	@ApiOperation(value = "获取报价单-项目列表", notes = "获取报价单-项目列表",hidden = true)
    @RequestMapping(value = "/getItemPage", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getItemPage(Long id,String bsStyle) {
        String method = "quote/getItemPage";String methodName ="获取报价单-项目列表";
        try {
            ApiResponseResult result = quoteService.getItemPage(id,bsStyle);
            logger.debug("获取报价单-项目列表=getItemPage:");
            getSysLogService().success(module,method, methodName, id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价单-项目列表失败！", e);
            getSysLogService().error(module,method, methodName, id+e.toString());
            return ApiResponseResult.failure("获取报价单-项目列表失败！");
        }
    }
	
	@ApiOperation(value = "编辑报价单", notes = "编辑报价单", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody Quote quote) {
		String method = "quote/edit";
		String methodName = "编辑报价单";
		try {
			ApiResponseResult result = quoteService.edit(quote);
			logger.debug("编辑报价单=edit:");
			getSysLogService().success(module, method, methodName, quote.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单编辑失败！", e);
			getSysLogService().error(module, method, methodName, quote.toString() + "," + e.toString());
			return ApiResponseResult.failure("报价单编辑失败！");
		}
	}
	
	@ApiOperation(value = "根据ID获取报价单数据", notes = "根据ID获取报价单数据", hidden = true)
    @RequestMapping(value = "/getSingleAll", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getSingleAll(@RequestBody Map<String, Object> params){
        String method = "quote/getSingleAll";String methodName ="根据ID获取报价单数据";
        long id = Long.parseLong(params.get("id").toString()) ;
        try{
            ApiResponseResult result = quoteService.getSingleAll(id);
            logger.debug("根据ID获取报价单数据=getSingleAll:");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取报价单数据失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("获取不良类别失败！");
        }
    }
	
	@ApiOperation(value = "设置报价单状态", notes = "设置报价单状态", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
        String method = "quote/doStatus";String methodName ="设置报价单状态";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("bsStatus").toString());
            ApiResponseResult result = quoteService.doStatus(id, bsStatus);
            logger.debug("设置报价单状态=doStatus:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置报价单状态失败！", e);
            getSysLogService().error(module,method, methodName,params+":"+ e.toString());
            return ApiResponseResult.failure("设置报价单状态失败！");
        }
    }

}
