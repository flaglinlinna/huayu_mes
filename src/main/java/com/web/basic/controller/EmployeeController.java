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
import com.web.basic.entity.Employee;
import com.web.basic.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "员工信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/employee")
public class EmployeeController extends WebController{

	 @Autowired
	 private EmployeeService employeeService;
	 
	 @ApiOperation(value = "员工基础信息表结构", notes = "员工基础信息表结构"+Employee.TABLE_NAME)
	    @RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
		@ResponseBody
	    public Employee getEmployee(){
	        return new Employee();
	    }
	 
	 
	 @ApiOperation(value = "员工信息列表页", notes = "员工信息列表页", hidden = true)
	    @RequestMapping(value = "/toEmployee")
	    public String toEmployee(){
	        return "/web/basic/employee";
	    }
	    @ApiOperation(value = "获取员工信息列表", notes = "获取员工信息列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/employee/getList";String methodName ="获取员工信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = employeeService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取员工信息列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取员工信息列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取员工信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增员工信息", notes = "新增员工信息",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Employee employee) {
	        String method = "base/employee/add";String methodName ="新增员工信息";
	        try{
	            ApiResponseResult result = employeeService.add(employee);
	            logger.debug("新增员工信息=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("员工信息新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("员工信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑员工信息", notes = "编辑员工信息",hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Employee employee){
	        String method = "base/employee/edit";String methodName ="编辑员工信息";
	        try{
	            ApiResponseResult result = employeeService.edit(employee);
	            logger.debug("编辑员工信息=edit:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑员工信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑员工信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取员工信息", notes = "根据ID获取员工信息",hidden = true)
	    @RequestMapping(value = "/getEmployee", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getEmployee(@RequestBody Map<String, Object> params){
	        String method = "base/employee/getEmployee";String methodName ="根据ID获取员工信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = employeeService.getEmployee(id);
	            logger.debug("根据ID获取员工信息=getEmployee:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取员工信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取员工信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除员工信息", notes = "删除员工信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/employee/delete";String methodName ="删除员工信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = employeeService.delete(id);
	            logger.debug("删除员工信息=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除员工信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除员工信息失败！");
	        }
	    }

}
