package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.HardwareMater;
import com.web.quote.service.HardwareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Api(description = "五金材料信息模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "hardware")
public class HardwareController extends WebController {

	private String module = "五金材料信息";

	@Autowired
	private HardwareService hardwareService;

	@ApiOperation(value = "五金材料信息表结构", notes = "五金材料信息表结构" + HardwareMater.TABLE_NAME)
	@RequestMapping(value = "/getHardware", method = RequestMethod.GET)
	@ResponseBody
	public HardwareMater getHardware() {
		return new HardwareMater();
	}



	@ApiOperation(value = "五金材料信息列表页", notes = "五金材料信息列表页", hidden = true)
	@RequestMapping(value = "/toHardware")
	public String toHardware() {
		return "/web/quote/02produce/hardware";
	}

	@ApiOperation(value = "新增五金材料信息", notes = "新增五金材料信息", hidden = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult add(@RequestBody HardwareMater hardwareMater) {
		String method = "hardware/add";
		String methodName = "新增五金材料信息";
		try {
			ApiResponseResult result = hardwareService.add(hardwareMater);
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
	public ApiResponseResult edit(@RequestBody HardwareMater hardwareMater) {
		String method = "hardware/edit";
		String methodName = "编辑五金材料信息";
		try {
			ApiResponseResult result = hardwareService.edit(hardwareMater);
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
		String method = "hardware/delete";
		String methodName = "删除五金材料信息";
		try {
			long id = Long.parseLong(params.get("id").toString());
			ApiResponseResult result = hardwareService.delete(id);
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

	@ApiOperation(value = "获取五金材料列表", notes = "获取五金材料列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword) {
		String method = "hardware/getList";
		String methodName = "获取五金材料列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = hardwareService.getList(keyword, super.getPageRequest(sort));
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
			ApiResponseResult result = hardwareService.getListByPkQuote(pkQuote, super.getPageRequest(sort));
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
	public ApiResponseResult getExcel(MultipartFile[] file) {
		String method = "/hardware/importExcel";String methodName ="导入模板";
		try {
			logger.debug("导入模板=importExcel:");
			getSysLogService().success(module,method, methodName, "");
			return hardwareService.doExcel(file);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入模板失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return null;
		}
	}


}
