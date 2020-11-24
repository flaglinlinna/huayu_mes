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
import com.web.basic.entity.WorkCenter;
import com.web.basic.service.WorkCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工作中心信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/center")
public class WorkCenterController extends WebController{

	private String module = "工作中心信息";

	 @Autowired
	 private WorkCenterService workCenterService;
	 
	 @ApiOperation(value = "工作中心信息表结构", notes = "工作中心信息表结构"+WorkCenter.TABLE_NAME)
	    @RequestMapping(value = "/getWorkCenter", method = RequestMethod.GET)
		@ResponseBody
	    public WorkCenter getWorkCenter(){
	        return new WorkCenter();
	    }
	 
	 @ApiOperation(value = "工作中心列表页", notes = "工作中心列表页", hidden = true)
	    @RequestMapping(value = "/toWorkCenter") 
	    public String toWorkCenter(){
	        return "/web/basic/center";
	    }
	    @ApiOperation(value = "获取工作中心列表", notes = "获取工作中心列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/center/getList";String methodName ="获取工作中心列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = workCenterService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取工作中心列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取工作中心列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取工作中心列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增工作中心", notes = "新增工作中心", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody WorkCenter center) {
	        String method = "base/center/add";String methodName ="新增工作中心";
	        try{
	            ApiResponseResult result = workCenterService.add(center);
	            logger.debug("新增工作中心=add:");
	            getSysLogService().success(module,method, methodName, center.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("工作中心新增失败！", e);
	            getSysLogService().error(module,method, methodName,center.toString()+";"+ e.toString());
	            return ApiResponseResult.failure("工作中心新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑工作中心", notes = "编辑工作中心", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody WorkCenter center){
	        String method = "base/center/edit";String methodName ="编辑工作中心";
	        try{
	            ApiResponseResult result = workCenterService.edit(center);
	            logger.debug("编辑工作中心=edit:");
	            getSysLogService().success(module,method, methodName, center.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑工作中心失败！", e);
	            getSysLogService().error(module,method, methodName, center.toString()+";"+e.toString());
	            return ApiResponseResult.failure("编辑工作中心失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取工作中心", notes = "根据ID获取工作中心", hidden = true)
	    @RequestMapping(value = "/getWorkCenter", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getWorkCenter(@RequestBody Map<String, Object> params){
	        String method = "base/center/getWorkCenter";String methodName ="根据ID获取工作中心";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = workCenterService.getWorkCenter(id);
	            logger.debug("根据ID获取工作中心=getWorkCenter:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取工作中心失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("获取工作中心失败！");
	        }
	    }
		
		@ApiOperation(value = "删除工作中心", notes = "删除工作中心", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/center/delete";String methodName ="删除工作中心";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = workCenterService.delete(id);
	            logger.debug("删除工作中心=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除工作中心失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("删除工作中心失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/center/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("bsStatus").toString());
		            ApiResponseResult result = workCenterService.doStatus(id, bsStatus);
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
