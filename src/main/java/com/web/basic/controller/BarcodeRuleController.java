package com.web.basic.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.BarcodeRule;
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

	private String module = "小码校验规则信息";
	
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
//	        	System.out.println(keyword);
	            Sort sort =  Sort.unsorted();
	            ApiResponseResult result = ruleService.getListByPrc(keyword, super.getPageRequest(sort));
	            logger.debug("获取小码校验规则信息列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取小码校验规则信息列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
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
	            getSysLogService().success(module,method, methodName, rule.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("小码校验规则信息新增失败！", e);
	            getSysLogService().error(module,method, methodName, rule.toString()+";"+e.toString());
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
	            //getSysLogService().success(module,method, methodName, JSON.toJSONString(rule));
	            getSysLogService().success(module,method, methodName, rule.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑小码校验规则信息失败！", e);
	            getSysLogService().error(module,method, methodName, rule.toString()+ e.toString());
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
	            //getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取小码校验规则信息失败！", e);
	            getSysLogService().error(module,method, methodName,"ID："+id+";"+ e.toString());
	            return ApiResponseResult.failure("获取小码校验规则信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除小码校验规则信息", notes = "删除小码校验规则信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/rule/delete";String methodName ="删除小码校验规则信息";
	        try{
//	        	long id = Long.parseLong(params.get("id").toString()) ;
				String ids = params.get("id").toString() ;
	            ApiResponseResult result = ruleService.delete(ids);
	            logger.debug("删除小码校验规则信息=delete:");
	            getSysLogService().success(module,method, methodName, "ID："+ids);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除小码校验规则信息失败！", e);
	            getSysLogService().error(module,method, methodName, "ID："+params+";"+e.toString());
	            return ApiResponseResult.failure("删除小码校验规则信息失败！");
	        }
	    }
		
		 @ApiOperation(value = "获取物料列表", notes = "获取物料列表", hidden = true)
		    @RequestMapping(value = "/getMtrialList", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult getMtrialList() {
		        String method = "base/rule/getMtrialList";String methodName ="获取物料列表";
		        try {
		            ApiResponseResult result = ruleService.getMtrialList();
		            logger.debug("获取物料列表=getMtrialList:");
		            //getSysLogService().success(module,method, methodName, null);
		            return result;
		        } catch (Exception e) {
		            e.printStackTrace();
		            logger.error("获取物料列表失败！", e);
		            getSysLogService().error(module,method, methodName, e.toString());
		            return ApiResponseResult.failure("获取物料列表失败！");
		        }
		    }


	@ApiOperation(value = "获取客户列表", notes = "获取客户列表", hidden = true)
	@RequestMapping(value = "/getCustomerList", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getCustomerList(String keyword) {
		String method = "base/rule/getCustomerList";String methodName ="获取客户列表";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = ruleService.getCustomer(keyword,super.getPageRequest(sort));
			logger.debug("获取客户列表=getCustomerList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取客户列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键词:"+keyword+";"+ e.toString());
			return ApiResponseResult.failure("获取客户列表失败！");
		}
	}

	@ApiOperation(value = "获取年月日下拉列表", notes = "获取年月日下拉列表", hidden = true)
	@RequestMapping(value = "/getBarList", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getBarList(String type) {
		String method = "base/rule/getBarList";String methodName ="获取年月日下拉列表";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = ruleService.getBarList(type,super.getPageRequest(sort));
			logger.debug("获取年月日下拉列表=getBarList:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取年月日下拉列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键词:"+type+";"+ e.toString());
			return ApiResponseResult.failure("获取年月日下拉列表失败！");
		}
	}

	@ApiOperation(value = "获取条码样例", notes = "获取条码样例", hidden = true)
	@RequestMapping(value = "/getFsampleByForm", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getFsampleByForm(@RequestBody Map<String, Object> param) {
		String method = "base/rule/getFsampleByForm";String methodName ="获取条码样例";
		String fixValue = param.get("fixValue").toString() ;
		String fyear = param.get("fyear").toString() ;
		String fmonth = param.get("fmonth").toString() ;
		String fday = param.get("fday").toString() ;
		String serialNum = param.get("serialNum").toString() ;
		String serialLen = param.get("serialLen").toString() ;
		try {
			ApiResponseResult result = ruleService.getFsampleByForm(fixValue,fyear,fmonth,fday,serialNum,serialLen);
			logger.debug("获取条码样例=getFsampleByForm:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取条码样例失败！", e);
			getSysLogService().error(module,method, methodName,e.toString());
			return ApiResponseResult.failure("获取条码样例失败！");
		}
	}

	@ApiOperation(value = "获取物料列表", notes = "获取物料列表", hidden = true)
	@RequestMapping(value = "/getMtrialListPage", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult getMtrialList(String keyword) {
		String method = "base/rule/getMtrialList";String methodName ="获取物料列表";
		try {
//			Sort sort = new Sort(Sort.Direction.DESC, "id");
//			ApiResponseResult result = ruleService.getMtrialList(keyword,super.getPageRequest(sort));
			Sort sort = Sort.unsorted();
			ApiResponseResult result = ruleService.getMtrial(keyword,super.getPageRequest(sort));
			logger.debug("获取物料列表=getMtrialList:");
//			getSysLogService().success(module,method, methodName, "关键词:"+keyword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取物料列表失败！", e);
			getSysLogService().error(module,method, methodName,"关键词:"+keyword+";"+ e.toString());
			return ApiResponseResult.failure("获取物料列表失败！");
		}
	}

}
