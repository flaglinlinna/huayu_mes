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
import com.web.basic.entity.WoLine;
import com.web.basic.service.WoLineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "线体信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/line")
public class WoLineController extends WebController{

	 @Autowired
	 private WoLineService woLineService;
	 
	 @ApiOperation(value = "线体列表页", notes = "线体列表页", hidden = true)
	    @RequestMapping(value = "/toWoLine")
	    public String toWoLine(){
	        return "/web/basic/line";
	    }
	    @ApiOperation(value = "获取线体列表", notes = "获取线体列表")
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/line/getList";String methodName ="获取线体列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = woLineService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取线体列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取线体列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取线体列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增线体", notes = "新增线体")
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody WoLine line) {
	        String method = "base/line/add";String methodName ="新增线体";
	        try{
	            ApiResponseResult result = woLineService.add(line);
	            logger.debug("新增线体=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("线体新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("线体新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑线体", notes = "编辑线体")
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody WoLine line){
	        String method = "base/line/edit";String methodName ="编辑线体";
	        try{
	            ApiResponseResult result = woLineService.edit(line);
	            logger.debug("编辑线体=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑线体失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑线体失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取线体", notes = "根据ID获取线体")
	    @RequestMapping(value = "/getWoLine", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getWoLine(@RequestBody Map<String, Object> params){
	        String method = "base/line/getWoLine";String methodName ="根据ID获取线体";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = woLineService.getWoLine(id);
	            logger.debug("根据ID获取线体=getWoLine:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取线体失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取线体失败！");
	        }
	    }
		
		@ApiOperation(value = "删除线体", notes = "删除线体")
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/line/delete";String methodName ="删除线体";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = woLineService.delete(id);
	            logger.debug("删除线体=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除线体失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除线体失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用")
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/line/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("bsStatus").toString());
		            ApiResponseResult result = woLineService.doStatus(id, bsStatus);
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
