package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import com.web.quote.service.ProductMaterService;
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

@Api(description = "制造部材料信息模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "productMater")
public class ProductMaterController extends WebController {

	private String module = "制造部材料信息";

	@Autowired
	private ProductMaterService productMaterService;

	@ApiOperation(value = "五金材料信息表结构", notes = "五金材料信息表结构" + ProductMater.TABLE_NAME)
	@RequestMapping(value = "/getProductMater", method = RequestMethod.GET)
	@ResponseBody
	public ProductMater getHardware() {
		return new ProductMater();
	}



	@ApiOperation(value = "五金材料信息列表页", notes = "五金材料信息列表页", hidden = true)
	@RequestMapping(value = "/toProductMater")
	public ModelAndView toProductMater(String bsType,String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("bsType", bsType);
			mav.addObject("quoteId", quoteId);
			mav.addObject("bomNameList",productMaterService.getBomSelect(quoteId));
			mav.setViewName("/web/quote/02produce/product_mater");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制造部材料信息失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "制造部材料价格列表页", notes = "制造部材料价格列表页", hidden = true)
	@RequestMapping(value = "/toProductPrice")
	public ModelAndView toProductPrice(String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("quoteId", quoteId);
			mav.setViewName("/web/quote/02produce/product_price");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制造部材料价格信息失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "新增五金材料信息", notes = "新增五金材料信息", hidden = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult add(@RequestBody ProductMater hardwareMater) {
		String method = "productMater/add";
		String methodName = "新增五金材料信息";
		try {
			ApiResponseResult result = productMaterService.add(hardwareMater);
			logger.debug("新增五金材料信息=add:");
			getSysLogService().success(module, method, methodName, hardwareMater.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("五金材料信息新增失败！", e);
			getSysLogService().error(module, method, methodName, hardwareMater.toString() + "," + e.toString());
			return ApiResponseResult.failure("五金材料信息新增失败！");
		}
	}

	@ApiOperation(value = "编辑五金材料信息", notes = "编辑五金材料信息", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody ProductMater hardwareMater) {
		String method = "productMater/edit";
		String methodName = "编辑五金材料信息";
		try {
			ApiResponseResult result = productMaterService.edit(hardwareMater);
			logger.debug("编辑五金材料信息=edit:");
			getSysLogService().success(module, method, methodName, hardwareMater.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("五金材料信息编辑失败！", e);
			getSysLogService().error(module, method, methodName, hardwareMater.toString() + "," + e.toString());
			return ApiResponseResult.failure("五金材料信息编辑失败！");
		}
	}

	@ApiOperation(value = "删除五金材料信息", notes = "删除五金材料信息", hidden = true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "productMater/delete";
		String methodName = "删除五金材料信息";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = productMaterService.delete(id);
			logger.debug("删除五金材料信息=delete:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除五金材料信息失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("删除五金材料信息失败！");
		}
	}

	@ApiOperation(value = "确认完成", notes = "确认完成", hidden = true)
	@RequestMapping(value = "/Confirm", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult Confirm(@RequestBody Map<String, Object> params) {
		String method = "/productMater/Confirm";
		String methodName = "确认完成";
		try {
			long id = Long.parseLong(params.get("id").toString());
			String bsType = params.get("bsType").toString();
			ApiResponseResult result = productMaterService.Confirm(id,bsType);
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

	@ApiOperation(value = "获取五金材料列表", notes = "获取五金材料列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String bsType,String quoteId) {
		String method = "/productMater/getList";
		String methodName = "获取五金材料列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = productMaterService.getList(keyword,bsType,quoteId, super.getPageRequest(sort));
			logger.debug("获取五金材料列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取五金材料列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取五金材料列表失败！");
		}
	}

	@ApiOperation(value = "获取报价单下五金材料列表", notes = "获取报价单下五金材料列表", hidden = true)
	@RequestMapping(value = "/getListByPkQuote", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getListByPkQuote(Long pkQuote) {
		String method = "hardware/getListByPkQuote";
		String methodName = "获取报价单下五金材料列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = productMaterService.getListByPkQuote(pkQuote, super.getPageRequest(sort));
			logger.debug("获取报价单下五金材料列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价单下五金材料列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+pkQuote==null?";":pkQuote+";"+e.toString());
			return ApiResponseResult.failure("获取报价单下五金材料列表失败！");
		}
	}

	@ApiOperation(value="导入模板", notes="导入模板", hidden = true)
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getExcel(MultipartFile[] file,String bsType,Long pkQuote) {
		String method = "/productMater/importExcel";String methodName ="导入模板";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return productMaterService.doExcel(file,bsType,pkQuote);
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
			productMaterService.exportExcel(response,bsType,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出数据失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
		}
	}


}
