package com.web.produce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "生产报工")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "/product")
public class ProductController extends WebController {

    private String module = "生产报工信息";
	 @Autowired
	 private ProductService productService;

    @ApiOperation(value = "生产报工页", notes = "生产投料页", hidden = true)
    @RequestMapping(value = "/toProductAdd")
    public String toProductAdd(){
        return "/web/produce/product/product_add";
    }

    
    @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getTaskNo(String keyword) {
        String method = "/product/getTaskNo";String methodName ="获取指令单信息";
        try {
            ApiResponseResult result = productService.getTaskNo(keyword);
            logger.debug("获取指令单信息=getTaskNo:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("获取指令单信息失败！", e);
            getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
             return ApiResponseResult.failure("获取指令单信息失败！");
        }
    }
    
    @ApiOperation(value="获取合箱制令单信息", notes="获取合箱制令单信息", hidden = true)
    @RequestMapping(value = "/getHXTaskNo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getHXTaskNo(String keyword ,String taskNo) {
        String method = "/product/getHXTaskNo";String methodName ="获取合箱制令单信息";
        try {
            ApiResponseResult result = productService.getHXTaskNo(taskNo,keyword);
            logger.debug("获取合箱制令单信息=getHXTaskNo:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("获取合箱制令单信息失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("获取合箱制令单信息失败！");
        }
    }
    
    @ApiOperation(value="内箱条码扫描后执行", notes="内箱条码扫描后执行", hidden = true)
    @RequestMapping(value = "/afterNei", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult afterNei(String barcode,String task_no) {
        String method = "/product/afterNei";String methodName ="内箱条码扫描后执行";
        try {
            ApiResponseResult result = productService.afterNei(barcode,task_no);
            logger.debug("内箱条码扫描后执行=afterNei:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("内箱条码扫描后执行失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("内箱条码扫描后执行失败！");
        }
    }
    
    @ApiOperation(value="外箱条码扫描后执行", notes="外箱条码扫描后执行", hidden = true)
    @RequestMapping(value = "/afterWai", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult afterWai(String nbarcode,String task_no,String wbarcode,String hx,String ptype) {
        String method = "/product/afterWai";String methodName ="外箱条码扫描后执行";
        try {
            ApiResponseResult result = productService.afterWai(nbarcode,task_no,wbarcode, hx, ptype);
            logger.debug("外箱条码扫描后执行=afterWai:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("外箱条码扫描后执行失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("外箱条码扫描后执行失败！");
        }
    }
    
    @ApiOperation(value="根据指令单获取扫描信息", notes="根据指令单获取扫描信息", hidden = true)
    @RequestMapping(value = "/getDetailByTask", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getDetailByTask(String taskNo) {
        String method = "/product/getDetailByTask";String methodName ="根据指令单获取扫描信息";
        try {
            ApiResponseResult result = productService.getDetailByTask(taskNo);
            logger.debug("根据指令单获取扫描信息=getDetailByTask:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("根据指令单获取扫描信息失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("根据指令单获取扫描信息失败！");
        }
    }
    
    @ApiOperation(value = "获取历史列表", notes = "获取历史列表")
    @RequestMapping(value = "/getHistoryList", method = RequestMethod.GET)
    @ResponseBody
	public ApiResponseResult getHistoryList(
			@RequestParam(value = "hkeywork", required = false) String hkeywork,
			@RequestParam(value = "hStartTime", required = false) String hStartTime,
			@RequestParam(value = "hEndTime", required = false) String hEndTime){
	  String method = "product/getHistoryList";String methodName ="获取历史列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result =productService.getHistoryList(hkeywork,hStartTime,hEndTime, super.getPageRequest(sort));
            logger.debug(methodName+"=getList:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
	}
    

}
