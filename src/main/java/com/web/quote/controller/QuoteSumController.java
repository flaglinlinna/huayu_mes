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

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

	@ApiOperation(value="导出数据", notes="导出数据", hidden = true)
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	@ResponseBody
	public void exportExcel(HttpServletResponse response, Long pkQuote) {
		String method = "/quoteSum/exportExcel";String methodName ="导出数据";
		try {
			logger.debug("导出数据=exportExcel:");
			getSysLogService().success(module,method, methodName, "");
			quoteSumService.exportExcel(response,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出数据失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
		}
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

	@ApiOperation(value = "修改管理费率", notes = "修改管理费率", hidden = true)
	@RequestMapping(value = "/updateBsManageFee", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult updateBsManageFee(@RequestBody Map<String, Object> params) {
		long pkQuote = Long.parseLong(params.get("pkQuote").toString()) ;
		BigDecimal bsManageFee = new BigDecimal(params.get("bsManageFee").toString());
		String method = "/quoteSum/updateBsManageFee";
		String methodName = "修改管理费率";
		try {
			ApiResponseResult result = quoteSumService.updateBsManageFee(pkQuote,bsManageFee);
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "修改包装运输费", notes = "修改包装运输费", hidden = true)
	@RequestMapping(value = "/updateBsFreight", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult updateBsFreight(@RequestBody Map<String, Object> params) {
		long pkQuote = Long.parseLong(params.get("pkQuote").toString()) ;
		BigDecimal bsFreight = new BigDecimal(params.get("bsFreight").toString());
		String method = "/quoteSum/updateBsFreight";
		String methodName = "修改包装运输费";
		try {
			ApiResponseResult result = quoteSumService.updateBsFreight(pkQuote,bsFreight);
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "修改毛利率", notes = "修改修改毛利率包装运输费", hidden = true)
	@RequestMapping(value = "/updateMlRate", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult updateMlRate(@RequestBody Map<String, Object> params) {
		long pkQuote = Long.parseLong(params.get("pkQuote").toString()) ;
		BigDecimal bsFreight = new BigDecimal(params.get("ml_rate").toString());
		String method = "/quoteSum/updateMlRate";
		String methodName = "修改毛利率";
		try {
			ApiResponseResult result = quoteSumService.updateMlRate(pkQuote,bsFreight);
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}


	@ApiOperation(value="设置中标",notes="设置中标",hidden=true)
	@RequestMapping(value="/setBade",method=RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult setBade(@RequestBody Map<String, Object> params){
		String method = "/quoteSum/setBade";
		String methodName = "设置中标";
		long quoteId = Long.parseLong(params.get("quoteId").toString()) ;
		Integer bsBade = Integer.parseInt(params.get("bsBade").toString());
		try {
			ApiResponseResult result = quoteSumService.setBade(Long.valueOf(quoteId),bsBade);
			logger.debug(methodName+"=setBade:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,quoteId);
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}


	@ApiOperation(value="价格计算",notes="价格计算",hidden=true)
	@RequestMapping(value="/testSum",method=RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult testSum(String quoteId){
		String method = "/quoteSum/testSum";
		String methodName = "价格计算";
		try {
			ApiResponseResult result = quoteSumService.countMeterAndProcess(quoteId);
			logger.debug(methodName+"=setBade:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,quoteId);
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "获取损耗明细列表", notes = "获取损耗明细列表", hidden = true)
	@RequestMapping(value = "/getSumList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getSumList(String quoteId) {
		String method = "/quoteSum/getSumList";
		String methodName = "获取损耗明细列表";
		try {
			ApiResponseResult result = quoteSumService.getSumList(Long.parseLong(quoteId),super.getPageRequest(Sort.unsorted()));
			logger.debug("获取损耗明细列表列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取损耗明细列表失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取损耗明细列表失败！");
		}
	}

	@ApiOperation(value = "获取包装运输费列表", notes = "获取包装运输费列表", hidden = true)
	@RequestMapping(value = "/getFreightList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getFreightList(String quoteId) {
		String method = "/quoteSum/getFreightList";
		String methodName = "获取包装运输费列表";
		try {
			ApiResponseResult result = quoteSumService.getFreightList(Long.parseLong(quoteId),super.getPageRequest(Sort.unsorted()));
			logger.debug("获取包装运输费列表=getFreightList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取包装运输费列表失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取包装运输费列表失败！");
		}
	}
}
