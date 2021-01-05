package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductProcess;
import com.web.quote.service.ProductProcessService;
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

@Api(description = "报价工艺流程信息模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "productProcess")
public class ProductProcessController extends WebController {

	private String module = "报价工艺流程信息";

	@Autowired
	private ProductProcessService productProcessService;

	@ApiOperation(value = "报价工艺流程表结构", notes = "报价工艺流程表结构" + ProductProcess.TABLE_NAME)
	@RequestMapping(value = "/getProductProcess", method = RequestMethod.GET)
	@ResponseBody
	public ProductProcess getProductProcess() {
		return new ProductProcess();
	}



	@ApiOperation(value = "报价工艺流程列表页", notes = "报价工艺流程列表页", hidden = true)
	@RequestMapping(value = "/toProductProcess")
	public ModelAndView toProductProcess(String bsType,String quoteId,String bsCode) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("bsType", bsType);
			mav.addObject("quoteId", quoteId);
			mav.addObject("bsCode", bsCode);
			mav.addObject("bomNameList",productProcessService.getBomSelect(quoteId));
			mav.setViewName("/web/quote/02produce/product_process");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程信息失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "新增报价工艺流程信息", notes = "新增报价工艺流程信息", hidden = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult add(@RequestBody ProductProcess productProcess) {
		String method = "productProcess/add";
		String methodName = "新增报价工艺流程信息";
		try {
			ApiResponseResult result = productProcessService.add(productProcess);
			logger.debug("新增报价工艺流程信息=add:");
			getSysLogService().success(module, method, methodName, productProcess.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价工艺流程信息新增失败！", e);
			getSysLogService().error(module, method, methodName, productProcess.toString() + "," + e.toString());
			return ApiResponseResult.failure("报价工艺流程信息新增失败！");
		}
	}

	@ApiOperation(value = "编辑报价工艺流程信息", notes = "编辑报价工艺流程信息", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody ProductProcess productProcess) {
		String method = "productProcess/edit";
		String methodName = "编辑报价工艺流程信息";
		try {
			ApiResponseResult result = productProcessService.edit(productProcess);
			logger.debug("编辑报价工艺流程信息=edit:");
			getSysLogService().success(module, method, methodName, productProcess.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报价工艺流程信息编辑失败！", e);
			getSysLogService().error(module, method, methodName, productProcess.toString() + "," + e.toString());
			return ApiResponseResult.failure("报价工艺流程信息编辑失败！");
		}
	}

	@ApiOperation(value = "删除报价工艺流程信息", notes = "删除报价工艺流程信息", hidden = true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "productProcess/delete";
		String methodName = "删除报价工艺流程信息";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = productProcessService.delete(id);
			logger.debug("删除报价工艺流程信息=delete:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除报价工艺流程信息失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("删除报价工艺流程信息失败！");
		}
	}

//	@ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表", hidden = true)
//	@RequestMapping(value = "/getBomByQuoteId", method = RequestMethod.GET)
//	@ResponseBody
//	public ApiResponseResult getList(String keyword,String bsType,String quoteId) {
//		String method = "/productProcess/getList";
//		String methodName = "获取报价工艺流程列表";
//		try {
//			Sort sort = new Sort(Sort.Direction.DESC, "id");
//			ApiResponseResult result = productProcessService.getList(keyword,bsType,quoteId, super.getPageRequest(sort));
//			logger.debug("获取报价工艺流程列表=getList:");
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("获取报价工艺流程列表失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
//			return ApiResponseResult.failure("获取报价工艺流程列表失败！");
//		}
//	}

	@ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String bsType,String quoteId) {
		String method = "/productProcess/getList";
		String methodName = "获取报价工艺流程列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = productProcessService.getList(keyword,bsType,quoteId, super.getPageRequest(sort));
			logger.debug("获取报价工艺流程列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取报价工艺流程列表失败！");
		}
	}

	@ApiOperation(value = "获取报价单下报价工艺流程列表", notes = "获取报价单下报价工艺流程列表", hidden = true)
	@RequestMapping(value = "/getListByPkQuote", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getListByPkQuote(Long pkQuote) {
		String method = "productProcess/getListByPkQuote";
		String methodName = "获取报价单下报价工艺流程列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = productProcessService.getListByPkQuote(pkQuote, super.getPageRequest(sort));
			logger.debug("获取报价单下报价工艺流程列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价单下报价工艺流程列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+pkQuote==null?";":pkQuote+";"+e.toString());
			return ApiResponseResult.failure("获取报价单下报价工艺流程列表失败！");
		}
	}

	@ApiOperation(value="导入模板", notes="导入模板", hidden = true)
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getExcel(MultipartFile[] file,String bsType,Long pkQuote) {
		String method = "/productProcess/importExcel";String methodName ="导入模板";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return productProcessService.doExcel(file,bsType,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}

	@ApiOperation(value="导出数据", notes="导出数据", hidden = true)
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	@ResponseBody
	public void exportExcel(HttpServletResponse response, String bsType, Long pkQuote) {
		String method = "/productProcess/exportExcel";String methodName ="导出数据";
		try {
			logger.debug("导出数据=exportExcel:");
			getSysLogService().success(module,method, methodName, "");
			productProcessService.exportExcel(response,bsType,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出数据失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
		}
	}

	@ApiOperation(value = "确认完成", notes = "确认完成", hidden = true)
	@RequestMapping(value = "/doStatus", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) {
		String method = "/productProcess/doStatus";
		String methodName = "确认完成";
		try {
			long id = Long.parseLong(params.get("id").toString());
			String bsType = params.get("bsType").toString();
			String bsCode = params.get("bsCode").toString();
			ApiResponseResult result = productProcessService.doStatus(id,bsType,bsCode);
			logger.debug("确认完成=Confirm:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("确认完成失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("确认完成信息失败！");
		}
	}

	@ApiOperation(value="从临时表导入数据", notes="从临时表导入数据", hidden = true)
	@RequestMapping(value = "/uploadCheck", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult uploadCheck(@RequestBody Map<String, Object> params) {
		String method = "/productProcess/uploadCheck";String methodName ="导出数据";
		try {
			Long pkQuote = Long.parseLong(params.get("pkQuote").toString());
			String bsType = params.get("bsType").toString();
			logger.debug("导入临时表数据=uploadCheck:");
			getSysLogService().success(module,method, methodName, "");
			return  productProcessService.uploadCheck(pkQuote,bsType);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出数据失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("失败！");
		}
	}



}
