package com.web.project.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.project.entity.ProjectDuty;
import com.web.project.service.ProjectDutyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "项目日常档案信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/projectDuty")
public class ProjectDutyController extends WebController{

	private String module = "项目日常档案信息";

	 @Autowired
	 private ProjectDutyService projectDutyService;
	 
	 @ApiOperation(value = "项目日常档案信息表结构", notes = "项目日常档案信息表结构"+ ProjectDuty.TABLE_NAME)
	    @RequestMapping(value = "/getProjectDuty", method = RequestMethod.GET)
		@ResponseBody
	    public ProjectDuty getProjectDuty(){
	        return new ProjectDuty();
	    }
	 
	 
	 @ApiOperation(value = "客户信息列表页", notes = "客户信息列表页", hidden = true)
	    @RequestMapping(value = "/toProjectDuty")
	    public String toProjectDuty(){
	        return "/web/project/projectDuty";
	    }

	    @ApiOperation(value = "获取客户信息列表", notes = "获取客户信息列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "projectDuty/getList";String methodName ="获取客户信息列表";
	        try {
//	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.ASC, "id");
	            ApiResponseResult result = projectDutyService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取客户信息列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取客户信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取客户信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增客户信息", notes = "新增客户信息",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody ProjectDuty client) {
	        String method = "projectDuty/add";String methodName ="新增客户信息";
	        try{
	            ApiResponseResult result = projectDutyService.add(client);
	            logger.debug("新增客户信息=add:");
	            getSysLogService().success(module,method, methodName, client.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("客户信息新增失败！", e);
	            getSysLogService().error(module,method, methodName, client.toString()+';'+e.toString());
	            return ApiResponseResult.failure("客户信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑客户信息", notes = "编辑客户信息",hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody ProjectDuty client){
	        String method = "projectDuty/edit";String methodName ="编辑客户信息";
	        try{
	            ApiResponseResult result = projectDutyService.edit(client);
	            logger.debug("编辑客户信息=edit:");
	            getSysLogService().success(module,method, methodName, client.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑客户信息失败！", e);
	            getSysLogService().error(module,method, methodName, client.toString()+';'+e.toString());
	            return ApiResponseResult.failure("编辑客户信息失败！");
	        }
	    }

//		@ApiOperation(value = "根据ID获取客户信息", notes = "根据ID获取客户信息",hidden = true)
//	    @RequestMapping(value = "/getClient", method = RequestMethod.POST)
//	    @ResponseBody
//	    public ApiResponseResult getClient(@RequestBody Map<String, Object> params){
//	        String method = "projectDuty/getClient";String methodName ="根据ID获取客户信息";
//	        long id = Long.parseLong(params.get("id").toString()) ;
//	        try{
//	            ApiResponseResult result = clientService.getClient(id);
//	            logger.debug("根据ID获取客户信息=getClient:");
////	            getSysLogService().success(module,method, methodName, null);
//	            return result;
//	        }catch (Exception e){
//	            e.printStackTrace();
//	            logger.error("根据ID获取客户信息失败！", e);
//	            getSysLogService().error(module,method, methodName,"ID:"+id+ e.toString());
//	            return ApiResponseResult.failure("获取客户信息失败！");
//	        }
//	    }
		
		@ApiOperation(value = "删除客户信息", notes = "删除客户信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "projectDuty/delete";String methodName ="删除客户信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = projectDutyService.delete(id);
	            logger.debug("删除客户信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除客户信息失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("删除客户信息失败！");
	        }
	    }

}
