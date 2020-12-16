package com.system.check.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;

import com.system.check.entity.WorkflowStep;
import com.system.check.service.WorkflowStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "流程步骤模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/check/WorkflowStep")
public class WorkflowStepController extends WebController{

	private String module = "流程步骤信息";

	 @Autowired
	 private WorkflowStepService workflowStepService;

	 	@ApiOperation(value = "流程步骤信息表结构", notes = "流程步骤信息表结构"+ WorkflowStep.TABLE_NAME)
	    @RequestMapping(value = "/getWorkflowStep", method = RequestMethod.GET)
		@ResponseBody
	    public WorkflowStep getWorkflowStep(){
	        return new WorkflowStep();
	    }



	    @ApiOperation(value = "获取流程步骤信息列表", notes = "获取流程步骤信息列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(Long mid) {
	        String method = "check/WorkflowStep/getList";String methodName ="获取流程步骤信息列表";
	        try {
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = workflowStepService.getList(mid, super.getPageRequest(sort));
	            logger.debug("获取流程步骤信息列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取流程步骤信息列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+mid==null?";":mid+";"+e.toString());
	            return ApiResponseResult.failure("获取流程步骤信息列表失败！");
	        }
	    }


	    @ApiOperation(value = "新增流程步骤信息", notes = "新增流程步骤信息", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody WorkflowStep workflowStep) {
	        String method = "check/WorkflowStep/add";String methodName ="新增流程步骤信息";
	        try{
	            ApiResponseResult result = workflowStepService.add(workflowStep);
	            logger.debug("新增流程步骤信息=add:");
	            getSysLogService().success(module,method, methodName, workflowStep.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("新增流程步骤信息失败！", e);
	            getSysLogService().error(module,method, methodName, workflowStep.toString()+";"+e.toString());
	            return ApiResponseResult.failure("新增流程步骤信息失败！");
	        }
	    }

	    @ApiOperation(value = "编辑流程信息", notes = "编辑流程信息", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody WorkflowStep workflowStep){
	        String method = "check/WorkflowStep/edit";String methodName ="编辑生产异常原因";
	        try{
	            ApiResponseResult result = workflowStepService.edit(workflowStep);
	            logger.debug("编辑流程步骤信息=edit:");
	            getSysLogService().success(module,method, methodName, workflowStep.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑流程步骤信息失败！", e);
	            getSysLogService().error(module,method, methodName, workflowStep.toString()+";"+e.toString());
	            return ApiResponseResult.failure("编辑流程步骤信息失败！");
	        }
	    }



		@ApiOperation(value = "删除流程步骤信息", notes = "删除流程步骤信息", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "check/WorkflowStep/delete";String methodName ="删除生产异常原因";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = workflowStepService.delete(id);
	            logger.debug("删除流程步骤信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除流程步骤信息失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("删除流程步骤信息失败！");
	        }
	    }

}
