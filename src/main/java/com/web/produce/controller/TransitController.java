package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.TransitService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "中转送检模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/transit")
public class TransitController extends WebController {

    private String module = "中转送检信息";
	@Autowired
	private TransitService transitService;

	@ApiOperation(value = "中转送检页", notes = "中转送检页", hidden = true)
	@RequestMapping(value = "/toTransit")
	public String toTransit() {
		return "/web/produce/transit/transit";
	}
	
	@ApiOperation(value="获取送验节点列表", notes="获取送验节点列表", hidden = true)
    @RequestMapping(value = "/getProc", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getProc(String keyword) {
        String method = "produce/transit/getProc";String methodName ="获取送验节点列表";
        try {
            ApiResponseResult result = transitService.getProc(keyword);
            logger.debug("获取送验节点列表=getProc:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("获取送验节点列表失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("获取送验节点列表失败！");
        }
    }
	
	@ApiOperation(value="获取送检类型列表", notes="获取送检类型列表", hidden = true)
    @RequestMapping(value = "/getType", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getType(String keyword) {
        String method = "produce/transit/getType";String methodName ="获取送检类型列表";
        try {
            ApiResponseResult result = transitService.getType(keyword);
            logger.debug("获取送检类型列表=getType:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("获取送检类型列表失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("获取送检类型列表失败！");
        }
    }
	
	@ApiOperation(value="检查箱号条码", notes="检查箱号条码", hidden = true)
    @RequestMapping(value = "/checkBarcode", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult checkBarcode(@RequestBody Map<String, Object> params) {
        String method = "produce/transit/checkBarcode";String methodName ="检查箱号条码";
        try {
        	String proc = params.get("proc") == null?"":params.get("proc").toString();
        	String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
            ApiResponseResult result = transitService.checkBarcode(proc,barcode);
            logger.debug("检查箱号条码信息=checkBarcode:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("检查箱号条码失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("检查箱号条码失败！");
        }
    }
	
	@ApiOperation(value="保存送检数据", notes="保存送检数据", hidden = true)
    @RequestMapping(value = "/saveData", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult saveData(@RequestBody Map<String, Object> params) {
        String method = "produce/transit/saveData";String methodName ="保存送检数据";
        try {
        	String proc = params.get("proc") == null?"":params.get("proc").toString();
        	String type = params.get("type") == null?"":params.get("type").toString();
        	String barcode = params.get("barcode") == null?"":params.get("barcode").toString();
            ApiResponseResult result = transitService.saveData(proc,type,barcode);
            logger.debug("保存送检数据信息=saveData:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("保存送检数据失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("保存送检数据失败！");
        }
    }
	
	@ApiOperation(value = "获取历史列表", notes = "获取历史列表")
    @RequestMapping(value = "/getHistoryList", method = RequestMethod.GET)
    @ResponseBody
	public ApiResponseResult getHistoryList(
			@RequestParam(value = "hkeywork", required = false) String hkeywork,
			@RequestParam(value = "hStartTime", required = false) String hStartTime,
			@RequestParam(value = "hEndTime", required = false) String hEndTime){
	  String method = "/product/transit/getHistoryList";String methodName ="获取历史列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result =transitService.getHistoryList(hkeywork,hStartTime,hEndTime, super.getPageRequest(sort));
            logger.debug(methodName+"=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
	}
}
