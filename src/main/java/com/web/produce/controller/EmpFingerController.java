package com.web.produce.controller;

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
import com.web.produce.entity.EmpFinger;
import com.web.produce.service.EmpFingerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "指纹登记信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "produce/emp_finger")
public class EmpFingerController extends WebController{

	 private String module = "指纹登陆信息";
	 @Autowired
	 private EmpFingerService empFingerService;
	 
	 @ApiOperation(value = "指纹登记表结构", notes = "指纹登记表结构"+EmpFinger.TABLE_NAME)
	    @RequestMapping(value = "/getEmpFinger", method = RequestMethod.GET)
		@ResponseBody
	    public EmpFinger getEmpFinger(){
	        return new EmpFinger();
	    }
	 
	 
	 @ApiOperation(value = "指纹登记信息列表页", notes = "指纹登记信息列表页", hidden = true)
	    @RequestMapping(value = "/toEmpFinger")
	    public String toEmpFinger(){
	        return "/web/produce/dev_clock/emp_finger";
	    }
	 
	    @ApiOperation(value = "获取指纹登记列表", notes = "获取指纹登记列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "produce/emp_finger/getList";String methodName ="获取指纹登记信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "emp.id");
	            ApiResponseResult result = empFingerService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取指纹登记列表=getList:");
	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取指纹登记列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取指纹登记列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增指纹登记信息", notes = "新增指纹登记信息",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody EmpFinger empFinger) {
	        String method = "produce/emp_finger/add";String methodName ="新增指纹登记信息";
	        try{
	            ApiResponseResult result = empFingerService.add(empFinger);
	            logger.debug("新增指纹登记信息=add:");
	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("指纹登记信息新增失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("指纹登记信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑指纹登记信息", notes = "编辑指纹登记信息",hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody EmpFinger empFinger){
	        String method = "produce/emp_finger/edit";String methodName ="编辑指纹登记信息";
	        try{
	            ApiResponseResult result = empFingerService.edit(empFinger);
	            logger.debug("编辑指纹登记信息=edit:");
	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑指纹登记信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("编辑指纹登记信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取指纹登记信息", notes = "根据ID获取指纹登记信息",hidden = true)
	    @RequestMapping(value = "/getEmpFinger", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getEmpFinger(@RequestBody Map<String, Object> params){
	        String method = "produce/emp_finger/getEmpFinger";String methodName ="根据ID获取指纹登记信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = empFingerService.getEmpFinger(id);
	            logger.debug("根据ID获取指纹登记信息=getEmpFinger:");
	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取指纹登记信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取指纹登记信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除指纹登记信息", notes = "删除指纹登记信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "produce/emp_finger/delete";String methodName ="删除指纹登记信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = empFingerService.delete(id);
	            logger.debug("删除指纹登记信息=delete:");
	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除指纹登记信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("删除指纹登记信息失败！");
	        }
	    }
		
		@ApiOperation(value = "获取线体信息列表", notes = "获取线体信息列表", hidden = true)
	    @RequestMapping(value = "/getEmpList", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getEmpList() {
	        String method = "produce/emp_finger/getEmpList";String methodName ="获取线体信息列表";
	        try {
	            ApiResponseResult result = empFingerService.getEmpList();
	            logger.debug("获取线体信息列表=getEmpList:");
	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取线体信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取线体信息列表失败！");
	        }
	    }
		
		@ApiOperation(value = "打开指纹仪", notes = "打开指纹仪",hidden = true)
	    @RequestMapping(value = "/open", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult open(@RequestBody Map<String, Object> params){
	        String method = "produce/emp_finger/open";String methodName ="打开指纹仪";
	        try{
	            ApiResponseResult result = empFingerService.open();
	            logger.debug("打开指纹仪=open:");
	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("打开指纹仪失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("打开指纹仪失败！");
	        }
	    }
		
}
