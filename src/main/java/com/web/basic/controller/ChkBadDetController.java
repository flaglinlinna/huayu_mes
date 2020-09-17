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
import com.web.basic.entity.ChkBadDet;
import com.web.basic.service.ChkBadDetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "不良内容信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/baddet")
public class ChkBadDetController extends WebController{

	 @Autowired
	 private ChkBadDetService chkBadDetService;
	 
	 @ApiOperation(value = "不良内容列表页", notes = "不良内容列表页", hidden = true)
	    @RequestMapping(value = "/toChkBadDet")
	    public String toChkBadDet(){
	        return "/web/basic/baddet";
	    }
	    @ApiOperation(value = "获取不良内容列表", notes = "获取不良内容列表")
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/baddet/getList";String methodName ="获取不良内容列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "ChkBad.id");
	            ApiResponseResult result = chkBadDetService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取不良内容列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取不良内容列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取不良内容列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增不良内容", notes = "新增不良内容")
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody ChkBadDet baddet) {
	        String method = "base/baddet/add";String methodName ="新增不良内容";
	        try{
	            ApiResponseResult result = chkBadDetService.add(baddet);
	            logger.debug("新增不良内容=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("不良内容新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("不良内容新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑不良内容", notes = "编辑不良内容")
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody ChkBadDet baddet){
	        String method = "base/baddet/edit";String methodName ="编辑不良内容";
	        try{
	            ApiResponseResult result = chkBadDetService.edit(baddet);
	            logger.debug("编辑不良内容=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑不良内容失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑不良内容失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取不良内容", notes = "根据ID获取不良内容")
	    @RequestMapping(value = "/getChkBadDet", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getChkBadDet(@RequestBody Map<String, Object> params){
	        String method = "base/baddet/getChkBadDet";String methodName ="根据ID获取不良内容";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = chkBadDetService.getChkBadDet(id);
	            logger.debug("根据ID获取不良内容=getChkBadDet:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取不良内容失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取不良内容失败！");
	        }
	    }
		
		 @ApiOperation(value = "获取不良类别列表", notes = "获取不良类别列表")
		    @RequestMapping(value = "/getChkBadList", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult getChkBadList() {
		        String method = "base/baddet/getChkBadList";String methodName ="获取不良类别列表";
		        try {
		            ApiResponseResult result = chkBadDetService.getChkBadList();
		            logger.debug("获取不良类别列表=getChkBadList:");
		            getSysLogService().success(method, methodName, null);
		            return result;
		        } catch (Exception e) {
		            e.printStackTrace();
		            logger.error("获取不良类别列表失败！", e);
		            getSysLogService().error(method, methodName, e.toString());
		            return ApiResponseResult.failure("获取不良类别列表失败！");
		        }
		    }
		
		@ApiOperation(value = "删除不良内容", notes = "删除不良内容")
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/baddet/delete";String methodName ="删除不良内容";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = chkBadDetService.delete(id);
	            logger.debug("删除不良内容=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除不良内容失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除不良内容失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用")
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/baddet/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("bsStatus").toString());
		            ApiResponseResult result = chkBadDetService.doStatus(id, bsStatus);
		            logger.debug("设置正常/禁用=doJob:");
		            getSysLogService().success(method, methodName, null);
		            return result;
		        }catch (Exception e){
		            e.printStackTrace();
		            logger.error("设置正常/禁用失败！", e);
		            getSysLogService().error(method, methodName, e.toString());
		            return ApiResponseResult.failure("设置正常/禁用失败！");
		        }
		    }
}
