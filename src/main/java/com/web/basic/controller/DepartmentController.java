package com.web.basic.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Department;
import com.web.basic.service.DepartmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "部门信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/department")
public class DepartmentController extends WebController{

	 @Autowired
	 private DepartmentService departmentService;
	 
	 @ApiOperation(value = "部门列表页", notes = "部门列表页", hidden = true)
	    @RequestMapping(value = "/toDepartmentList")
	    public String toDepartmentList(){
	        return "/basic/departmentList";
	    }
	    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "/department/getList";String methodName ="获取部门列表";
	        try {
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = departmentService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取部门列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取部门列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取部门列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增部门", notes = "新增部门")
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(Department department) {
	        String method = "/department/add";String methodName ="新增部门";
	        System.out.println("/department/add");
	        try{
	            ApiResponseResult result = departmentService.add(department);
	            logger.debug("新增部门=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("部门新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("部门新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑部门", notes = "编辑部门")
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(Department department){
	        String method = "/department/edit";String methodName ="编辑部门";
	        try{
	            ApiResponseResult result = departmentService.edit(department);
	            logger.debug("编辑部门=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑部门失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑部门失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取部门", notes = "根据ID获取部门")
	    @RequestMapping(value = "/getDepartment", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getDepartment(Long id){
	        String method = "/department/getDepartment";String methodName ="根据ID获取部门";
	        try{
	            ApiResponseResult result = departmentService.getDepartment(id);
	            logger.debug("根据ID获取部门=getDepartment:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取部门失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取部门失败！");
	        }
	    }
		
		@ApiOperation(value = "删除部门", notes = "删除部门")
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestParam(value = "id", required = false) Long id){
	        String method = "/department/delete";String methodName ="删除部门";
	        try{
	            ApiResponseResult result = departmentService.delete(id);
	            logger.debug("删除部门=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除部门失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除部门失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用")
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(Long id, Integer deStatus) throws Exception{
		        String method = "/department/doStatus";String methodName ="设置正常/禁用";
		        try{
		            ApiResponseResult result = departmentService.doStatus(id, deStatus);
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
