package com.web.quote.controller;

import javax.servlet.http.HttpServletResponse;

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
import com.web.quote.entity.ProductMater;
import com.web.quote.service.OutService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "外协部报价模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "out")
public class OutController extends WebController {

	private String module = "外协部报价模块";

	@Autowired
	private OutService outService;
	
	@ApiOperation(value = "外协部报价单页", notes = "外协部报价单页", hidden = true)
	@RequestMapping(value = "/toOutList")
	public ModelAndView toOutList() {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("Style", "out");
			mav.setViewName("/web/quote/03purchase/out_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("外协部报价单页失败！", e);
		}
		return mav;
	}


	@ApiOperation(value = "获取报价单列表", notes = "获取报价单列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String bsStatus) {
		String method = "/out/getQuoteList";
		String methodName = "获取报价单列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = outService.getList(keyword,bsStatus, super.getPageRequest(sort));
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

	@ApiOperation(value = "获取采购部材料价格填写列表", notes = "获取采购部材料价格填写列表", hidden = true)
	@RequestMapping(value = "/getQuoteList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getQuoteList(String keyword,String quoteId) {
		String method = "/productMater/getQuoteList";
		String methodName = "获取采购部材料价格填写列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = outService.getQuoteList(keyword,quoteId, super.getPageRequest(sort));
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}


	@ApiOperation(value = "报价单信息编辑", notes = "报价单信息编辑", hidden = true)
	@RequestMapping(value = "/edit")
	public ApiResponseResult edit(@RequestBody ProductMater hardwareMater) {
		String method = "productMater/edit";
		String methodName = "编辑报价单信息";
		try {
			ApiResponseResult result = outService.edit(hardwareMater);
			logger.debug("编辑报价单信息=edit:");
			getSysLogService().success(module, method, methodName, hardwareMater.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价单信息编辑失败！", e);
			getSysLogService().error(module, method, methodName, hardwareMater.toString() + "," + e.toString());
			return ApiResponseResult.failure("报价单信息编辑失败！");
		}
	}

	@ApiOperation(value="导出数据", notes="导出数据", hidden = true)
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	@ResponseBody
	public void exportExcel(HttpServletResponse response,Long pkQuote) {
		String method = "/purchase/exportExcel";String methodName ="导出数据";
		try {
			logger.debug("导出数据=exportExcel:");
			getSysLogService().success(module,method, methodName, "");
			outService.exportExcel(response,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出数据失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
		}
	}

		@ApiOperation(value="导入模板", notes="导入模板", hidden = true)
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getExcel(MultipartFile[] file,Long pkQuote) {
		String method = "/purchase/importExcel";String methodName ="导入模板";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
//			return null;
			return outService.doExcel(file,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}

}
