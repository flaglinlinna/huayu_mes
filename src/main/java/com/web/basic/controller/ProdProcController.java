package com.web.basic.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.service.ProdProcService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工艺流程")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/prodproc")
public class ProdProcController extends WebController{
	
	@Autowired
	 private ProdProcService procProdService;
	
	@ApiOperation(value = "工艺流程列表页", notes = "工艺流程列表页", hidden = true)
    @RequestMapping(value = "/toProdProc")
    public String toProdProc(){
        return "/web/basic/prodproc";
    }

	@ApiOperation(value = "获取工艺流程列表", notes = "获取工艺流程列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "base/prodproc/getList";String methodName ="获取工艺流程列表";
        try {
        	System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = procProdService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取工艺流程列表=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺流程列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取工艺流程列表失败！");
        }
    }
	
	@ApiOperation(value = "获取工艺流程-工序列表", notes = "获取工艺流程-工序列表")
    @RequestMapping(value = "/getDetailList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getDetailList(String keyword) {
        String method = "base/prodproc/getDetailList";String methodName ="获取工艺流程-工序列表";
        try {
        	System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = procProdService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取工艺流程-工序列表=getDetailList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺流程-工序列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取工艺流程-工序列表失败！");
        }
    }
	
	@ApiOperation(value = "获取物料和工序表", notes = "获取物料和工序表")
    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getData(){
        String method = "base/prodproc/getData";String methodName ="获取物料和工序表";
        try{
            ApiResponseResult result = procProdService.getData();
            logger.debug("获取物料和工序表=getData:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取物料和工序表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取物料和工序表失败！");
        }
    }
	
	@ApiOperation(value = "根据ID获取工艺流程", notes = "根据ID获取工艺流程")
    @RequestMapping(value = "/getProdProc", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getProdProc(@RequestBody Map<String, Object> params){
        String method = "base/prodproc/getProdProc";String methodName ="根据ID获取工艺流程";
        long id = Long.parseLong(params.get("id").toString()) ;
        try{
            ApiResponseResult result = procProdService.getProdProc(id);
            logger.debug("根据ID获取工艺流程=getProdProc:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取工艺流程失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取工艺流程失败！");
        }
    }
}
