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
import com.web.basic.entity.ChkBad;
import com.web.basic.service.ChkBadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "不良类别信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/bad")
public class ChkBadController extends WebController{

	 @Autowired
	 private ChkBadService chkBadService;
	 
	 @ApiOperation(value = "不良类别列表页", notes = "不良类别列表页", hidden = true)
	    @RequestMapping(value = "/toChkBad")
	    public String toChkBad(){
	        return "/web/basic/bad";
	    }
	    @ApiOperation(value = "获取不良类别列表", notes = "获取不良类别列表")
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/bad/getList";String methodName ="获取不良类别列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = chkBadService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取不良类别列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取不良类别列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取不良类别列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增不良类别", notes = "新增不良类别")
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody ChkBad bad) {
	        String method = "base/bad/add";String methodName ="新增不良类别";
	        try{
	            ApiResponseResult result = chkBadService.add(bad);
	            logger.debug("新增不良类别=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("不良类别新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("不良类别新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑不良类别", notes = "编辑不良类别")
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody ChkBad bad){
	        String method = "base/bad/edit";String methodName ="编辑不良类别";
	        try{
	            ApiResponseResult result = chkBadService.edit(bad);
	            logger.debug("编辑不良类别=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑不良类别失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑不良类别失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取不良类别", notes = "根据ID获取不良类别")
	    @RequestMapping(value = "/getChkBad", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getChkBad(@RequestBody Map<String, Object> params){
	        String method = "base/bad/getChkBad";String methodName ="根据ID获取不良类别";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = chkBadService.getChkBad(id);
	            logger.debug("根据ID获取不良类别=getChkBad:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取不良类别失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取不良类别失败！");
	        }
	    }
		
		@ApiOperation(value = "删除不良类别", notes = "删除不良类别")
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/bad/delete";String methodName ="删除不良类别";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = chkBadService.delete(id);
	            logger.debug("删除不良类别=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除不良类别失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除不良类别失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用")
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/bad/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("bsStatus").toString());
		            ApiResponseResult result = chkBadService.doStatus(id, bsStatus);
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
