package com.web.produce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("获取指令单信息失败！", e);
             getSysLogService().error(method, methodName, e.toString());
             return ApiResponseResult.failure("获取指令单信息失败！");
        }
    }
    
    @ApiOperation(value="获取合箱制令单信息", notes="获取合箱制令单信息", hidden = true)
    @RequestMapping(value = "/getHXTaskNo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getHXTaskNo(String keyword) {
        String method = "/product/getHXTaskNo";String methodName ="获取合箱制令单信息";
        try {
            ApiResponseResult result = productService.getHXTaskNo(keyword);
            logger.debug("获取合箱制令单信息=getHXTaskNo:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("获取合箱制令单信息失败！", e);
             getSysLogService().error(method, methodName, e.toString());
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
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("内箱条码扫描后执行失败！", e);
             getSysLogService().error(method, methodName, e.toString());
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
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("外箱条码扫描后执行失败！", e);
             getSysLogService().error(method, methodName, e.toString());
             return ApiResponseResult.failure("外箱条码扫描后执行失败！");
        }
    }
    

}
