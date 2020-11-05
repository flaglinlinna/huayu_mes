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

	private String module = "员工信息";

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
	    public ApiResponseResult getList(String keyword,String empStatus) {
	        String method = "base/employee/getList";String methodName ="获取员工信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.ASC, "empCode");
	            ApiResponseResult result = employeeService.getList(keyword,empStatus, super.getPageRequest(sort));
	            logger.debug("获取员工信息列表=getList:");
	            getSysLogService().success(module,method, methodName, keyword+";"+empStatus);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取员工信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
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
	            getSysLogService().success(module,method, methodName, employee.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("员工信息新增失败！", e);
	            getSysLogService().error(module,method, methodName,employee.toString()+";"+ e.toString());
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
	            getSysLogService().success(module,method, methodName, employee);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑员工信息失败！", e);
	            getSysLogService().error(module,method, methodName,employee+";"+ e.toString());
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
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取员工信息失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
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
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除员工信息失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("删除员工信息失败！");
	        }
	    }
		@ApiOperation(value = "设置在职/离职", notes = "设置在职/离职", hidden = true)
	    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
		 //Long id, Integer deStatus
	        String method = "base/employee/doStatus";String methodName ="设置在职/离职";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	        	Integer empStatus=Integer.parseInt(params.get("empStatus").toString());
	            ApiResponseResult result = employeeService.doStatus(id, empStatus);
	            logger.debug("设置在职/离职=doJob:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("设置在职/离职失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("设置在职/离职失败！");
	        }
	    }
		
		@ApiOperation(value = "同步员工信息", notes = "同步员工信息", hidden = true)
	    @RequestMapping(value = "/getUpdateData", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getUpdateData() throws Exception{
	        String method = "base/employee/getUpdateData";String methodName ="同步员工信息";
	        try{
	            ApiResponseResult result = employeeService.getUpdateData();
	            logger.debug("同步员工信息 getUpdateData:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("同步员工信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("同步员工信息失败！");
	        }
	    }

}
