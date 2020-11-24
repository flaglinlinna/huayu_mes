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
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.Line;
import com.web.basic.service.LineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "线体信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/line")
public class LineController extends WebController{
	
	private String module = "线体信息";

	 @Autowired
	 private LineService lineService;
	 
	 @ApiOperation(value = "线体列表页", notes = "线体列表页", hidden = true)
	    @RequestMapping(value = "/toLine")
	    public String toLine(){
	        return "/web/basic/line";
	    }
	    @ApiOperation(value = "获取线体列表", notes = "获取线体列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword,String lineNo,String linerName,String lastupdateDate,
	    		String checkStatus,String createDate,String linerCode,String lineName) {
	        String method = "base/line/getList";String methodName ="获取线体列表";
	        String param = "关键字:"+keyword+";线体编码:"+lineNo+";线体名称"+lineName+";线长工号"+linerCode+
	        		";线长姓名"+linerName+";状态:"+checkStatus+";更新时间:"+lastupdateDate+";添加时间:"+createDate;
	        try {
	        	System.out.println(param);
	            Sort sort = new Sort(Sort.Direction.ASC, "lineNo");
	            ApiResponseResult result = lineService.getList(keyword, lineNo, linerName, lastupdateDate,
	    	    		 checkStatus, createDate, linerCode, lineName, super.getPageRequest(sort));
	            logger.debug("获取线体列表=getList:");
//	            getSysLogService().success(module,method, methodName, param);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取线体列表失败！", e);
	            getSysLogService().error(module,method, methodName, param+";"+e.toString());
	            return ApiResponseResult.failure("获取线体列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增线体", notes = "新增线体", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Line line) {
	        String method = "base/line/add";String methodName ="新增线体";
	        try{
	            ApiResponseResult result = lineService.add(line);
	            logger.debug("新增线体=add:");
	            getSysLogService().success(module,method, methodName, line.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("线体新增失败！", e);
	            getSysLogService().error(module,method, methodName, line.toString()+","+e.toString());
	            return ApiResponseResult.failure("线体新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑线体", notes = "编辑线体", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Line line){
	        String method = "base/line/edit";String methodName ="编辑线体";
	        try{
	            ApiResponseResult result = lineService.edit(line);
	            logger.debug("编辑线体=edit:");
	            getSysLogService().success(module,method, methodName, line.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑线体失败！", e);
	            getSysLogService().error(module,method, methodName, line.toString()+","+e.toString());
	            return ApiResponseResult.failure("编辑线体失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取线体", notes = "根据ID获取线体", hidden = true)
	    @RequestMapping(value = "/getLine", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getLine(@RequestBody Map<String, Object> params){
	        String method = "base/line/getLine";String methodName ="根据ID获取线体";
	        String pa = "";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = lineService.getLine(id);
	            logger.debug("根据ID获取线体=getLine:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取线体失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("获取线体失败！");
	        }
	    }
		
		@ApiOperation(value = "删除线体", notes = "删除线体", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/line/delete";String methodName ="删除线体";
	        try{
	        	String ids = params.get("id").toString() ;
	            ApiResponseResult result = lineService.delete(ids);
	            logger.debug("删除线体=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除线体失败！", e);
	            getSysLogService().error(module,method, methodName, params+","+e.toString());
	            return ApiResponseResult.failure("删除线体失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/line/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
		            ApiResponseResult result = lineService.doStatus(id, bsStatus);
		            logger.debug("设置正常/禁用=doJob:");
		            getSysLogService().success(module,method, methodName, params);
		            return result;
		        }catch (Exception e){
		            e.printStackTrace();
		            logger.error("设置正常/禁用失败！", e);
		            getSysLogService().error(module,method, methodName, params+";"+e.toString());
		            return ApiResponseResult.failure("设置正常/禁用失败！");
		        }
		    }
}
