package com.web.basePrice.controller;

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
import com.web.basePrice.entity.BaseFee;
import com.web.basePrice.service.BaseFeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("人工制费维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/baseFee")
public class BaseFeeController extends WebController{

    private String module = "人工制费维护";
    @Autowired
    private BaseFeeService baseFeeService;

    @ApiOperation(value = "人工制费维护表结构", notes = "人工制费维护结构"+ BaseFee.TABLE_NAME)
    @RequestMapping(value = "/getBaseFee", method = RequestMethod.GET)
    @ResponseBody
    public BaseFee getBaseFee(){
        return new BaseFee();
    }

    @ApiOperation(value = "人工制费维护列表页", notes = "人工制费维护列表页", hidden = true)
    @RequestMapping(value = "/toBaseFee")
    public String toBaseFee(){
        return "/web/basePrice/base_fee";
    }

    @ApiOperation(value = "获取人工制费维护列表", notes = "获取人工制费维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/baseFee/getList";String methodName ="获取人工制费维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = baseFeeService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取人工制费维护列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取人工制费维护列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取人工制费维护列表失败！");
        }
    }

    @ApiOperation(value = "新增人工制费维护", notes = "新增人工制费维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody BaseFee baseFee) {
        String method = "basePrice/baseFee/add";String methodName ="新增人工制费维护";
        try{
            ApiResponseResult result = baseFeeService.add(baseFee);
            logger.debug("新增人工制费维护=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("人工制费维护新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("人工制费维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑人工制费维护", notes = "编辑人工制费维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody BaseFee baseFee){
        String method = "basePrice/baseFee/edit";String methodName ="编辑人工制费维护";
        try{
            ApiResponseResult result = baseFeeService.edit(baseFee);
            logger.debug("编辑人工制费维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑人工制费维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑人工制费维护失败！");
        }
    }

    @ApiOperation(value = "删除人工制费维护", notes = "删除人工制费维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/baseFee/delete";String methodName ="删除人工制费维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = baseFeeService.delete(id);
            logger.debug("删除人工制费维护=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除人工制费维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除人工制费维护失败！");
        }
    }
    
    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/baseFee/doStatus";String methodName ="设置正常/禁用";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
            ApiResponseResult result = baseFeeService.doStatus(id, bsStatus);
            logger.debug("设置正常/禁用=doStatus:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置正常/禁用失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("设置正常/禁用失败！");
        }
    }
    
    @ApiOperation(value = "获取工序信息", notes = "获取工序信息", hidden = true)
    @RequestMapping(value = "/getProcList", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getProcList(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/baseFee/getProcList";String methodName ="获取工序信息";
        try{
        	String type  = params.get("type").toString() ;
        	String condition  = params.get("condition").toString() ;
        	Sort sort = Sort.unsorted();
            ApiResponseResult result = baseFeeService.getProcList(type, condition,super.getPageRequest(sort));
            logger.debug("获取工序信息=getProcList:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取工序信息失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("获取工序信息失败！");
        }
    }
    
    @ApiOperation(value = "获取机台类型信息", notes = "获取机台类型信息", hidden = true)
    @RequestMapping(value = "/getType", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getType(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/baseFee/getType";String methodName ="获取机台类型信息";
        try{
        	String keyword  = params.get("keyword").toString() ;
        	Sort sort = Sort.unsorted();
            ApiResponseResult result = baseFeeService.getType(keyword,super.getPageRequest(sort));
            logger.debug("获取机台类型信息=getType:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取机台类型信息失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("获取机台类型信息失败！");
        }
    }
    
}
