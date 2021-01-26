package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteBom;
import com.web.quote.service.QuoteBomService;
import com.web.quote.service.QuoteService;
import com.web.quote.service.QuoteSumBomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Api(description = "虚拟料号对应模块")
@CrossOrigin
@ControllerAdvice
@Controller
	@RequestMapping(value = "quoteItem")
public class QuoteItemController extends WebController {

	private String module = "虚拟料号对应信息";

	@Autowired
	private QuoteSumBomService quoteSumBomService;
	
	@Autowired
	private QuoteService quoteService;

	@ApiOperation(value = "报价单汇总页", notes = "报价单汇总页", hidden = true)
	@RequestMapping(value = "/toQuoteList")
	public ModelAndView toQuoteList() {
		ModelAndView mav = new ModelAndView();
		try {
//			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/04summary/quote_item_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单列表失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "报价单汇总页", notes = "报价单汇总页", hidden = true)
	@RequestMapping(value = "/toQuoteItemList")
	public ModelAndView toQuoteItemList(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/04summary/quote_item");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单列表失败！", e);
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
		String method = "/quoteItem/getList";
		String methodName = "获取报价单列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = quoteSumBomService.getList(quoteId,keyword, bsStatus,bsCode,bsType,bsFinishTime,bsRemarks,
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

	@ApiOperation(value = "获取虚拟物料对应列表", notes = "获取虚拟物料对应列表", hidden = true)
	@RequestMapping(value = "/getItemList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getItemList(Long pkQuote,String keyword) {
		String method = "/quoteItem/getItemList";
		String methodName = "获取报价单列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = quoteSumBomService.getItemList(pkQuote,keyword,super.getPageRequest(sort));
			logger.debug(methodName+"=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value="导出数据", notes="导出数据", hidden = true)
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	@ResponseBody
	public void exportExcel(HttpServletResponse response, Long pkQuote) {
		String method = "/quoteItem/exportExcel";String methodName ="导出数据";
		try {
			logger.debug("导出数据=exportExcel:");
			getSysLogService().success(module,method, methodName, "");
			quoteSumBomService.exportExcel(response,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出数据失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
		}
	}

	@ApiOperation(value="虚拟料号对应导入模板", notes="导入模板", hidden = true)
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getExcel(MultipartFile[] file, Long pkQuote ) {
		String method = "/quoteItem/importExcel";String methodName ="虚拟料号对应导入模板";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return quoteSumBomService.doExcel(file,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("虚拟料号对应导入模板导入失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}

	@ApiOperation(value="编辑实际料号", notes="编辑实际料号", hidden = true)
	@RequestMapping(value = "/editBsItemCodeReal", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult editBsItemCodeReal(@RequestBody Map<String, Object> params) {
		long id = Long.parseLong(params.get("id").toString()) ;
		String bsItemCodeReal=params.get("bsItemCodeReal").toString();
		String method = "/quoteItem/editBsItemCodeReal";String methodName ="编辑实际料号";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return quoteSumBomService.editBsItemCodeReal(id,bsItemCodeReal);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("虚拟料号对应导入模板导入失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("虚拟料号对应导入模板导入失败");
		}
	}

	//实际料号对应确认完成
//	@ApiOperation(value="确认完成", notes="确认完成", hidden = true)
//	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
//	@ResponseBody
//	public ApiResponseResult confirm(@RequestBody Map<String, Object> params) {
//		Long quoteId = Long.parseLong(params.get("quoteId").toString()) ;
//		String method = "/quoteItem/confirm";String methodName ="确认完成";
//		try {
//			logger.debug("确认完成=confirm:");
//			getSysLogService().success(module,method, methodName, "");
//			return null;
////			return quoteSumBomService.confirm(id,bsItemCodeReal);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("确认完成失败！", e);
//			getSysLogService().error(module,method, methodName, e.toString());
//			return null;
//		}
//	}

}
