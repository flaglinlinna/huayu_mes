package com.web.basic.controller;

import java.util.Date;
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
import com.web.basic.entity.BarcodeRule;
import com.web.basic.entity.Mtrial;
import com.web.basic.service.BarcodeRuleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "小码校验规则信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/rule")
public class BarcodeRuleController extends WebController{

	 @Autowired
	 private BarcodeRuleService ruleService;
	 
	 @ApiOperation(value = "小码校验规则基础信息表结构", notes = "小码校验规则基础信息表结构"+BarcodeRule.TABLE_NAME)
	    @RequestMapping(value = "/getBarcodeRule", method = RequestMethod.GET)
		@ResponseBody
	    public BarcodeRule getBarcodeRule(){
	        return new BarcodeRule();
	    }
	 
	 
	 @ApiOperation(value = "小码校验规则信息列表页", notes = "小码校验规则信息列表页", hidden = true)
	    @RequestMapping(value = "/toBarcodeRule")
	    public String toBarcodeRule(){
	        return "/web/basic/rule";
	    }
	    @ApiOperation(value = "获取小码校验规则信息列表", notes = "获取小码校验规则信息列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/rule/getList";String methodName ="获取小码校验规则信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = ruleService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取小码校验规则信息列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取小码校验规则信息列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取小码校验规则信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增小码校验规则信息", notes = "新增小码校验规则信息",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody BarcodeRule rule) {
	        String method = "base/rule/add";String methodName ="新增小码校验规则信息";
	        try{
	            ApiResponseResult result = ruleService.add(rule);
	            logger.debug("新增小码校验规则信息=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("小码校验规则信息新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("小码校验规则信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑小码校验规则信息", notes = "编辑小码校验规则信息",hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody BarcodeRule rule){
	        String method = "base/rule/edit";String methodName ="编辑小码校验规则信息";
	        try{
	            ApiResponseResult result = ruleService.edit(rule);
	            logger.debug("编辑小码校验规则信息=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑小码校验规则信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑小码校验规则信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取小码校验规则信息", notes = "根据ID获取小码校验规则信息",hidden = true)
	    @RequestMapping(value = "/getBarcodeRule", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getBarcodeRule(@RequestBody Map<String, Object> params){
	        String method = "base/rule/getBarcodeRule";String methodName ="根据ID获取小码校验规则信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = ruleService.getBarcodeRule(id);
	            logger.debug("根据ID获取小码校验规则信息=getBarcodeRule:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取小码校验规则信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取小码校验规则信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除小码校验规则信息", notes = "删除小码校验规则信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/rule/delete";String methodName ="删除小码校验规则信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = ruleService.delete(id);
	            logger.debug("删除小码校验规则信息=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除小码校验规则信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除小码校验规则信息失败！");
	        }
	    }
		
		 @ApiOperation(value = "获取物料列表", notes = "获取物料列表", hidden = true)
		    @RequestMapping(value = "/getMtrialList", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult getMtrialList() {
		        String method = "base/defdetail/getMtrialList";String methodName ="获取物料列表";
		        try {
		            ApiResponseResult result = ruleService.getMtrialList();
		            logger.debug("获取物料列表=getMtrialList:");
		            getSysLogService().success(method, methodName, null);
		            return result;
		        } catch (Exception e) {
		            e.printStackTrace();
		            logger.error("获取物料列表失败！", e);
		            getSysLogService().error(method, methodName, e.toString());
		            return ApiResponseResult.failure("获取物料列表失败！");
		        }
		    }

}
