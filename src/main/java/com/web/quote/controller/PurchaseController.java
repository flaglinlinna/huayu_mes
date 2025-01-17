package com.web.quote.controller;

import java.util.List;
import java.util.Map;

import com.utils.TypeChangeUtils;
import com.web.quote.service.QuoteService;
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

import com.alibaba.druid.util.StringUtils;
import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import com.web.quote.service.PurchaseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(description = "采购部报价模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "purchase")
public class PurchaseController extends WebController {

	private String module = "采购部报价模块";

	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private QuoteService quoteService;
	
	@ApiOperation(value = "采购部报价单页", notes = "采购部报价单页", hidden = true)
	@RequestMapping(value = "/toPurchaseList")
	public ModelAndView toPurchaseList(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/03purchase/purchase_list");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制造部材料信息失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "采购部材料价格填写页", notes = "采购部材料价格填写页", hidden = true)
	@RequestMapping(value = "/toPurchaseEdite")
	public ModelAndView toPurchaseEdite(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult iStatus =purchaseService.getStatus(Long.parseLong(quoteId),0);
			mav.addObject("nowStatus", iStatus);
			mav.addObject("quoteId", quoteId);
			mav.addObject("bsStatus2", quoteService.getStatus2(Long.parseLong(quoteId)).getData());
			mav.addObject("bsStatus3", quoteService.getStatus3(Long.parseLong(quoteId)).getData());
			mav.setViewName("/web/quote/03purchase/purchase_edite");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("采购部材料价格填写页失败！", e);
		}
		return mav;
	}


	@ApiOperation(value = "获取报价单列表", notes = "获取报价单列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String quoteId,String keyword,String bsStatus,String bsCode,String bsType,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsProdType,String bsSimilarProd,
									 String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,String userName) {
		String method = "/purchase/getList";
		String methodName = "获取报价单列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = purchaseService.getList(quoteId,keyword, bsStatus,bsCode,bsType,bsFinishTime,bsRemarks,
					bsProd,bsProdType,bsSimilarProd,bsPosition,bsCustRequire,bsLevel,bsRequire,bsDevType,bsCustName,userName,super.getPageRequest(sort));
			logger.debug(methodName+"=getList:");
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
	public ApiResponseResult getQuoteList(String keyword,String quoteId,String bsAgent) {
		String method = "/purchase/getQuoteList";
		String methodName = "获取采购部材料价格填写列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = purchaseService.getQuoteList(keyword,quoteId,bsAgent, super.getPageRequest(sort));
			logger.debug(methodName+"=getQuoteList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}


	@ApiOperation(value = "取消完成", notes = "取消完成", hidden = true)
	@RequestMapping(value = "/cancelStatus", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult cancelStatus(@RequestBody Map<String, Object> params) {
		String method = "/productMater/cancelStatus";
		String methodName = "取消完成";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = purchaseService.cancelStatus(id);
			logger.debug("取消确认完成=Confirm:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("取消确认完成失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("取消确认完成失败！");
		}
	}

	@ApiOperation(value = "报价单信息编辑", notes = "报价单信息编辑", hidden = true)
	@RequestMapping(value = "/edit")
	@ResponseBody
	public ApiResponseResult edit(@RequestBody ProductMater hardwareMater) {
		String method = "purchase/edit";
		String methodName = "编辑报价单信息";
		try {
			ApiResponseResult result = purchaseService.edit(hardwareMater);
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
	public void exportExcel(HttpServletResponse response,Long pkQuote,String bsAgent) {
		String method = "/purchase/exportExcel";String methodName ="导出数据";
		try {
			logger.debug("导出数据=exportExcel:");
			getSysLogService().success(module,method, methodName, "");
			purchaseService.exportExcel(response,pkQuote,bsAgent);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出数据失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
		}
	}

	@ApiOperation(value = "更新单位", notes = "更新单位", hidden = true)
	@RequestMapping(value = "/updateUnit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult updateUnit(@RequestBody Map<String, Object> params) {
		String method = "/purchase/updateUnit";
		String methodName = "更新单位";
		try {
			long id = Long.parseLong(params.get("id").toString());
			String unitIdString = params.get("unitId").toString();
			ApiResponseResult result = purchaseService.updateUnit(id,unitIdString);
			logger.debug("更新单位=updateUnit:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新单位失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("更新单位信息失败！");
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
			return purchaseService.doExcel(file,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}

	@ApiOperation(value = "确认完成", notes = "确认完成", hidden = true)
	@RequestMapping(value = "/doStatus", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) {
		String method = "/purchase/doStatus";
		String methodName = "确认完成";
		long id = Long.parseLong(params.get("id").toString());
		try {
//			String bsType = params.get("bsType").toString();
			List<ProductMater> productMaterList = TypeChangeUtils.objectToList(params.get("dates"),ProductMater.class);
			ApiResponseResult result = purchaseService.doStatus(id,productMaterList);
			logger.debug("确认完成=doStatus:");
			getSysLogService().success(module,method, methodName, id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("确认完成失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("确认完成信息失败！");
		}
	}
	
	@ApiOperation(value = "选择价格档位", notes = "选择价格档位", hidden = true)
	@RequestMapping(value = "/doGear", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult doGear(String id,String gearPrice) {
		String method = "/purchase/doGear";
		String methodName = "选择价格档位";
		try {
			if(StringUtils.isEmpty(gearPrice)){
				return ApiResponseResult.failure("档位不能为空");
			}
			String[] strs = gearPrice.split(",");
			if(strs.length != 2){
				return ApiResponseResult.failure("档位格式不正确");
			}
			
			ApiResponseResult result = purchaseService.doGear(id,strs[1],strs[0]);
			logger.debug(methodName+"=:"+method);
			getSysLogService().success(module,method, methodName, id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}
	
	@ApiOperation(value = "采购部发起审批前校验", notes = "采购部发起审批前校验", hidden = true)
	@RequestMapping(value = "/doCheckBefore", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult doCheckBefore(String keyword,String quoteId) {
		String method = "/purchase/doCheckBefore";
		String methodName = "采购部发起审批前校验";
		try {
			ApiResponseResult result = purchaseService.doCheckBefore(keyword,quoteId);
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
