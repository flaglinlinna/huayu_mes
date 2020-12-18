package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteFile;
import com.web.quote.service.QuoteFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Api(description = "产品资料模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "productFile")
public class QuoteFileController extends WebController {

	private String module = "产品资料信息";

	@Autowired
	private QuoteFileService quoteFileService;

	
	@ApiOperation(value = "产品资料模块页", notes = "产品资料模块页", hidden = true)
	@RequestMapping(value = "/toProductFile")
	public ModelAndView toProductFile(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/01business/quote_file");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "新增产品资料模块", notes = "新增产品资料模块", hidden = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult add(@RequestBody QuoteFile productFile) {
		String method = "productFile/add";
		String methodName = "新增产品资料信息";
		try {
			ApiResponseResult result = quoteFileService.add(productFile);
			logger.debug("新增产品资料信息=edit:");
			getSysLogService().success(module, method, methodName, productFile.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("产品资料信息新增失败！", e);
			getSysLogService().error(module, method, methodName, productFile.toString() + "," + e.toString());
			return ApiResponseResult.failure("产品资料信息新增失败！");
		}
	}


	@ApiOperation(value = "获取产品资料信息列表", notes = "获取产品资料信息列表",hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String pkQuote) {
		String method = "productFile/getList";String methodName ="获取产品资料信息";
		try {
			Sort sort = new Sort(Sort.Direction.ASC, "id");
			ApiResponseResult result = quoteFileService.getList(keyword,pkQuote, super.getPageRequest(sort));
			logger.debug("获取产品资料信息列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产品资料信息列表失败！", e);
			getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
			return ApiResponseResult.failure("获取产品资料信息列表失败！");
		}
	}



	@ApiOperation(value = "删除产品资料信息列表", notes = "删除产品资料信息列表", hidden = true)
	@RequestMapping(value = "productFile/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "/delete";
		String methodName = "删除产品资料信息列表";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = quoteFileService.delete(id);
			logger.debug("删除产品资料信息列表=delete:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除产品资料信息列表失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("删除产品资料信息列表失败！");
		}
	}
}
