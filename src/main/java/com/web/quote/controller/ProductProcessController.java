package com.web.quote.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.utils.TypeChangeUtils;
import com.web.basic.service.SysParamSubService;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.ProductProcess;
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.ProductProcessService;
import com.web.quote.service.QuoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
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
	@Autowired
	private SysParamSubService sysParamSubService;
	@Autowired
	private QuoteService quoteService;

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

//			quoteService.getItemStatus(Long.parseLong(quoteId),bsCode);
//			quoteService.getOutStatus(Long.parseLong(quoteId));
			mav.addObject("bsType", bsType);
			mav.addObject("quoteId", quoteId);
			mav.addObject("bsCode", bsCode);
			if(!bsType.equals("out")){
				mav.addObject("nowStatus",quoteService.getItemStatus(Long.parseLong(quoteId),bsCode));
			}else {
				mav.addObject("nowStatus", quoteService.getOutStatus(Long.parseLong(quoteId)));
			}
			mav.addObject("bsStatus2", quoteService.getStatus2(Long.parseLong(quoteId)).getData());
			mav.addObject("Jitai", sysParamSubService.getListByMCode("BJ_BASE_MACHINE_TYPE").getData());
			mav.addObject("bomNameList",productProcessService.getBomSelect(quoteId));
			mav.setViewName("/web/quote/02produce/product_process");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程信息失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "报价工艺流程列表页", notes = "报价工艺流程列表页", hidden = true)
	@RequestMapping(value = "/toProductFreight")
	public ModelAndView toProductFreight(String bsType,String quoteId) {
		ModelAndView mav = new ModelAndView();
		try {

			mav.addObject("bsType", bsType);
			mav.addObject("quoteId", quoteId);
			mav.addObject("nowStatus", quoteService.getFreightStatus(Long.parseLong(quoteId)));
			mav.setViewName("/web/quote/02produce/product_freight");// 返回路径
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

	@ApiOperation(value = "编辑包装运输费附件信息", notes = "编辑包装运输费附件信息", hidden = true)
	@RequestMapping(value = "/editFileId", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult editFileId(@RequestBody Map<String, Object> params) {
		String method = "productProcess/editFileId";
		String methodName = "编辑包装运输费附件信息";
		try {
			Long fileId = null;
			Long id = Long.parseLong(params.get("id").toString());
			if(params.get("fileId")!=null&&!("").equals(params.get("fileId"))){
				fileId = Long.parseLong(params.get("fileId").toString());
			}
//			Long fileId = Long.parseLong(params.get("fileId")==null?.toString());
			String fileName = params.get("fileName")==null?"":params.get("fileName").toString();
			ApiResponseResult result = productProcessService.editFileId(id,fileId,fileName);
//			logger.debug("编辑报价工艺流程信息=edit:");
//			getSysLogService().success(module, method, methodName, productProcess.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("编辑包装运输费附件信息失败！", e);
//			getSysLogService().error(module, method, methodName, productProcess.toString() + "," + e.toString());
			return ApiResponseResult.failure("编辑包装运输费附件信息失败！");
		}
	}

	@ApiOperation(value = "删除报价工艺流程信息", notes = "删除报价工艺流程信息", hidden = true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params) {
		String method = "productProcess/delete";
		String methodName = "删除报价工艺流程信息";
		try {
			String id = params.get("id").toString();
			ApiResponseResult result = productProcessService.deleteIds(id);
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

	@ApiOperation(value = "获取报价Bom列表,下拉选择", notes = "获取报价Bom列表", hidden = true)
	@RequestMapping(value = "/getBomByQuoteId", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getBomByQuoteId(String keyword,String bsType,String quoteId) {
		String method = "/productProcess/getBomByQuoteId";
		String methodName = "获取报价工艺流程BOM列表";
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = productProcessService.getBomList(keyword,Long.parseLong(quoteId),bsType, super.getPageRequest(sort));
			logger.debug("获取报价工艺流程列表=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取报价工艺流程列表失败！");
		}
	}

	@ApiOperation(value = "获取关联材料名称", notes = "获取关联材料名称", hidden = true)
	@RequestMapping(value = "/getLinkByBsName", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getLinkByBsName(String bsName,String quoteId) {
//		String method = "/productProcess/getLinkByBsName";
//		String methodName = "获取关联材料名称";
		try {
//			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = productProcessService.getLinkNameList(Long.parseLong(quoteId),bsName);
			logger.debug("获取关联材料名称=getList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取关联材料名称失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取关联材料名称失败！");
		}
	}

	@ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表", hidden = true)
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getList(String keyword,String bsType,String quoteId) {
		String method = "/productProcess/getList";
		String methodName = "获取报价工艺流程列表";
		try {
//			Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "bsName");
//			Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "bsType");
			Sort.Order order3 = new Sort.Order(Sort.Direction.ASC, "bsOrder");
			List<Sort.Order> list = new ArrayList<>();
//			list.add(order1);
//			list.add(order2);
			list.add(order3);
			Sort sort = new Sort(list);
			if(bsType.equals("out")){
				sort = Sort.unsorted();
			}
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

	@ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表", hidden = true)
	@RequestMapping(value = "/getListByLose", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getListByLose(String keyword,String processId,String quoteId) {
		String method = "/productProcess/getListByLose";
		String methodName = "获取报价工艺流程列表";
		try {
			Sort.Order order3 = new Sort.Order(Sort.Direction.ASC, "bsOrder");
			List<Sort.Order> list = new ArrayList<>();
			list.add(order3);
			Sort sort = new Sort(list);
			ApiResponseResult result = productProcessService.getListByLose(keyword,processId,quoteId, super.getPageRequest(sort));
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
//			Sort sort = new Sort(Sort.Direction.DESC, "id");
//			Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "bsName");
			Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "bsOrder");
			List<Sort.Order> list = new ArrayList<>();
//			list.add(order1);
			list.add(order2);
			Sort sort = new Sort(list);
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
		long id = Long.parseLong(params.get("id").toString());
		String bsType = params.get("bsType").toString();
		String methodName = "确认完成";
		try {
			List<ProductProcess> quoteBomList = TypeChangeUtils.objectToList(params.get("dates"),ProductProcess.class);
			String bsCode = "";
			if(!("out").equals(bsType)){
				 bsCode = params.get("bsCode").toString();
			}
			ApiResponseResult result = productProcessService.doStatus(id,bsType,bsCode,quoteBomList);
			logger.debug("确认完成=Confirm:");
			getSysLogService().success(module,method, methodName, "ID:"+id+"bsType:"+bsType);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("确认完成失败！", e);
			getSysLogService().error(module,method, methodName,"ID:"+id+"bsType:"+bsType+":"+ e.toString());
			return ApiResponseResult.failure("确认完成信息失败！");
		}
	}

	@ApiOperation(value = "取消完成", notes = "取消完成", hidden = true)
	@RequestMapping(value = "/cancelStatus", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult cancelStatus(@RequestBody Map<String, Object> params) {
		String method = "/productProcess/cancelStatus";
		String methodName = "取消完成";
		try {
			long id = Long.parseLong(params.get("id").toString());
			String bsType = params.get("bsType").toString();
			String bsCode = "C005";
			if(!("out").equals(bsType)){
				bsCode = params.get("bsCode").toString();
			}
			ApiResponseResult result = productProcessService.cancelStatus(id,bsType,bsCode);
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
	
	@ApiOperation(value = "根据大类获取工序", notes = "根据大类获取工序", hidden = true)
	@RequestMapping(value = "/getProcListByType", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getProcListByType(String bsType) {
		String method = "productProcess/getProcListByType";
		String methodName = "根据大类获取工序";
		try {
			ApiResponseResult result = productProcessService.getProcListByType(bsType);
			logger.debug(methodName+"=getProcListByType:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+bsType==null?";":bsType+";"+e.toString());
			return ApiResponseResult.failure(methodName+"列表失败！");
		}
	}



	@ApiOperation(value = "修改机种类型", notes = "修改机种类型", hidden = true)
	@RequestMapping(value = "/updateModelType", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult updateModelType(@RequestBody Map<String, Object> params) {
		String method = "/productProcess/updateModelType";
		String methodName = "修改机种类型";
		try {
			long id = Long.parseLong(params.get("id").toString());
			String modelCode = params.get("modelCode").toString();
			ApiResponseResult result = productProcessService.updateModelType(id,modelCode);
			logger.debug("修改机种类型=updateModelType:");
			getSysLogService().success(module,method, methodName, params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改机种类型失败！", e);
			getSysLogService().error(module,method, methodName,params+":"+ e.toString());
			return ApiResponseResult.failure("修改机种类型失败！");
		}
	}


	@ApiOperation(value = "删除附件", notes = "删除附件",hidden = true)
	@RequestMapping(value = "/delFile", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delFile(Long recordId, Long fileId){
		String method = "/productProcess/delFile";String methodName ="删除附件";
		try{
			ApiResponseResult result = productProcessService.delFile(recordId,fileId);
			logger.debug("删除附件=delete:");
			getSysLogService().success(module,method, methodName, null);
			return result;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("删除附件失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("删除附件失败！");
		}
	}

	@ApiOperation(value = "页面编辑保存", notes = "页面编辑保存",hidden = true)
	@RequestMapping(value = "/saveTable", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult saveTable(@RequestBody List<ProductProcess> productProcessList) {
		String method = "quoteProcess/saveTable";String methodName ="页面编辑保存";
		try{
			ApiResponseResult result = productProcessService.editProcessList(productProcessList);
			logger.debug("页面编辑保存=saveTable:");
			getSysLogService().success(module,method, methodName,
					"");
			return result;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("页面编辑保存失败！", e);
			getSysLogService().error(module,method, methodName, e.toString());
			return ApiResponseResult.failure("页面编辑保存失败！");
		}
	}


}
