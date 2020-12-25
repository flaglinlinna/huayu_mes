package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;

import com.web.quote.entity.ProductMaterTemp;
import com.web.quote.service.ProductMaterTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Api(description = "制造部材料信息导入临时表模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "productMaterTemp")
public class ProductMaterTempController extends WebController {

	private String module = "制造部材料导入临时表";

	@Autowired
	private ProductMaterTempService tempService;

	@ApiOperation(value = "五金材料信息表结构", notes = "五金材料信息表结构" + ProductMaterTemp.TABLE_NAME)
	@RequestMapping(value = "/getProductMater", method = RequestMethod.GET)
	@ResponseBody
	public ProductMaterTemp getHardware() {
		return new ProductMaterTemp();
	}


	@ApiOperation(value = "编辑五金材料信息", notes = "编辑五金材料信息", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody ProductMaterTemp hardwareMater) {
		String method = "productMaterTemp/edit";
		String methodName = "编辑临时表信息";
		try {
			ApiResponseResult result = tempService.edit(hardwareMater);
			logger.debug("编辑临时表信息=edit:");
			getSysLogService().success(module, method, methodName, hardwareMater.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("临时表编辑失败！", e);
			getSysLogService().error(module, method, methodName, hardwareMater.toString() + "," + e.toString());
			return ApiResponseResult.failure("临时表信息编辑失败！");
		}
	}

	@ApiOperation(value = "删除五金材料信息", notes = "删除五金材料信息", hidden = true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "productMaterTemp/delete";
		String methodName = "删除临时表信息";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = tempService.delete(id);
			logger.debug("删除临时表信息=delete:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除临时表信息失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("删除临时表信息失败！");
		}
	}



	@ApiOperation(value = "获取导入临时列表", notes = "获取导入临时列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String bsType,String bsPurchase,String quoteId) {
		String method = "/productMaterTemp/getList";
		String methodName = "获取导入临时列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = tempService.getList(bsPurchase,bsType,quoteId, super.getPageRequest(sort));
			logger.debug("获取导入临时列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取导入临时列表失败！", e);
			getSysLogService().error(module,method, methodName,"");
			return ApiResponseResult.failure("获取导入临时列表失败！");
		}
	}

	@ApiOperation(value="导入制造部材料模板", notes="导入模板", hidden = true)
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getExcel(MultipartFile[] file,String bsType,Long pkQuote) {
		String method = "/productMaterTemp/importExcel";String methodName ="导入模板";
		try {
			logger.debug("导入制造部材料模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return tempService.doExcel(file,bsType,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入制造部材料模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}

	@ApiOperation(value="导入采购填报价格模板", notes="导入采购填报价格模板", hidden = true)
	@RequestMapping(value = "/importByPurchase", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult importByPurchase(MultipartFile[] file,Long pkQuote) {
		String method = "/productMaterTemp/importByPurchase";String methodName ="导入模板";
		try {
			logger.debug("导入采购填报价格模板=importByPurchase:");
			getSysLogService().success(module,method, methodName, "");
			return tempService.doExcel(file,pkQuote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入采购填报价格模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}

    @ApiOperation(value="采购填报价格确定导入正式表", notes="采购填报价格确定导入正式表", hidden = true)
    @RequestMapping(value = "/confirmUpload", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult confirmUpload(@RequestBody Map<String, Object> params) {
        String method = "/productMaterTemp/confirmUpload";String methodName ="导出数据";
        try {
            Long pkQuote = Long.parseLong(params.get("pkQuote").toString());
//            String bsType = params.get("bsType").toString();
            logger.debug("导入临时表数据=uploadCheck:");
            getSysLogService().success(module,method, methodName, "");
            return  tempService.importByTemp(pkQuote);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出数据失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("失败！");
        }
    }


}
