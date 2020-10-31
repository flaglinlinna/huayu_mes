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
import com.web.produce.service.InputService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "生产投料模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "/input")
public class InputController extends WebController {
	
	 @Autowired
	 private InputService inputService;

    @ApiOperation(value = "生产投料页", notes = "生产投料页", hidden = true)
    @RequestMapping(value = "/toInputAdd")
    public String toInputAdd(){
        return "/web/produce/input/input_add";
    }
    
    @ApiOperation(value = "生产投料列表页", notes = "生产投料列表页", hidden = true)
    @RequestMapping(value = "/toInputList")
    public String toInputList(){
        return "/web/produce/input/input_list";
    }
    
    @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getTaskNo(String keyword) {
        String method = "/input/getTaskNo";String methodName ="获取指令单信息";
        try {
            ApiResponseResult result = inputService.getTaskNo(keyword);
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
    
    @ApiOperation(value="根据指令单获取物料编号和数量", notes="根据指令单获取物料编号和数量", hidden = true)
    @RequestMapping(value = "/getInfoBarcode", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getInfoBarcode(String barcode) {
        String method = "/input/getInfoBarcode";String methodName ="获取指令单信息";
        try {
            ApiResponseResult result = inputService.getInfoBarcode(barcode);
            logger.debug("根据指令单获取物料编号和数量=getTaskNo:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("根据指令单获取物料编号和数量失败！", e);
             getSysLogService().error(method, methodName, e.toString());
             return ApiResponseResult.failure("根据指令单获取物料编号和数量失败！");
        }
    }
    
    @ApiOperation(value="确认投入", notes="确认投入", hidden = true)
    @RequestMapping(value = "/addPut", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult addPut(String barcode,String task_no,String item_no,String qty) {
        String method = "/input/addPut";String methodName ="确认投入";
        try {
            ApiResponseResult result = inputService.addPut(barcode,task_no,item_no,qty);
            logger.debug("确认投入=addPut:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("确认投入失败！", e);
             getSysLogService().error(method, methodName, e.toString());
             return ApiResponseResult.failure("确认投入失败！");
        }
    }
    
    @ApiOperation(value = "删除", notes = "删除",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult delete(String barcode){
        String method = "/input/delete";String methodName ="删除";
        try{
            ApiResponseResult result = inputService.delete(barcode);
            logger.debug("删除=delete:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("删除失败！");
        }
    }

    
    @ApiOperation(value="根据指令单获取扫描信息", notes="根据指令单获取扫描信息", hidden = true)
    @RequestMapping(value = "/getDetailByTask", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getDetailByTask(String taskNo) {
        String method = "/input/getDetailByTask";String methodName ="根据指令单获取扫描信息";
        try {
            ApiResponseResult result = inputService.getDetailByTask(taskNo);
            logger.debug("根据指令单获取扫描信息=getDetailByTask:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("根据指令单获取扫描信息失败！", e);
             getSysLogService().error(method, methodName, e.toString());
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
	  String method = "input/getHistoryList";String methodName ="获取历史列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result =inputService.getHistoryList(hkeywork,hStartTime,hEndTime, super.getPageRequest(sort));
            logger.debug(methodName+"=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
	}
}
