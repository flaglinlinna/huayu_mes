package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductProcessTemp;
import com.web.quote.service.ProductProcessTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Api(description = "报价工艺流程信息 临时导入模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "productProcessTemp")
public class ProductProcessTempController extends WebController {

	private String module = "报价工艺流程信息临时导入";

	@Autowired
	private ProductProcessTempService productProcessTempService;

	@ApiOperation(value = "报价工艺流程临时表结构", notes = "报价工艺流程临时表结构" + ProductProcessTemp.TABLE_NAME)
	@RequestMapping(value = "/getProductProcessTemp", method = RequestMethod.GET)
	@ResponseBody
	public ProductProcessTemp getProductProcessTemp() {
		return new ProductProcessTemp();
	}



	@ApiOperation(value = "编辑报价工艺流程信息", notes = "编辑报价工艺流程信息", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody ProductProcessTemp productProcess) {
		String method = "productProcessTemp/edit";
		String methodName = "编辑报价工艺流程信息";
		try {
			ApiResponseResult result = productProcessTempService.edit(productProcess);
			logger.debug("编辑报价工艺流程临时信息=edit:");
			getSysLogService().success(module, method, methodName, productProcess.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价工艺流程临时表信息编辑失败！", e);
			getSysLogService().error(module, method, methodName, productProcess.toString() + "," + e.toString());
			return ApiResponseResult.failure("报价工艺流程临时表信息编辑失败！");
		}
	}

	@ApiOperation(value = "删除报价工艺流程信息临时表", notes = "删除报价工艺流程临时表信息", hidden = true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "productProcessTemp/delete";
		String methodName = "删除报价工艺流程信息临时表";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = productProcessTempService.delete(id);
			logger.debug("删除报价工艺流程临时表信息=delete:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除报价工艺流程临时表信息失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("删除报价工艺流程临时表信息失败！");
		}
	}

	@ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String bsType,String quoteId) {
		String method = "/productProcessTemp/getList";
		String methodName = "获取报价工艺流程列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = productProcessTempService.getList(keyword,bsType,quoteId, super.getPageRequest(sort));
			logger.debug("获取报价工艺流程列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取报价工艺流程列表失败！");
		}
	}



	@ApiOperation(value="导入模板", notes="导入模板", hidden = true)
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getExcel(MultipartFile[] file,String bsType,Long pkQuote) {
		String method = "/productProcessTemp/importExcel";String methodName ="导入模板";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return productProcessTempService.doExcel(file,bsType,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}



}
