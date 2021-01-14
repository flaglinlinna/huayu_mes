package com.system.check.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.check.entity.Workflow;
import com.system.check.service.WorkflowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "流程模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/check/Workflow")
public class WorkflowController extends WebController{

	private String module = "流程信息";

	 @Autowired
	 private WorkflowService workflowService;

	 	@ApiOperation(value = "流程信息表结构", notes = "流程信息表结构"+ Workflow.TABLE_NAME)
	    @RequestMapping(value = "/getWorkflow", method = RequestMethod.GET)
		@ResponseBody
	    public Workflow getWorkflow(){
	        return new Workflow();
	    }


	 	@ApiOperation(value = "流程信息列表页", notes = "流程信息列表页", hidden = true)
	    @RequestMapping(value = "/toWorkflow")
	    public String toWorkflow(){
	        return "/system/workflow/workflow";
	    }
	    @ApiOperation(value = "获取流程信息列表", notes = "获取流程信息列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "check/Workflow/getList";String methodName ="获取流程信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = workflowService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取流程信息列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取流程信息列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取流程信息列表失败！");
	        }
	    }


	    @ApiOperation(value = "新增流程信息", notes = "新增流程信息", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Workflow workflow) {
	        String method = "check/Workflow/add";String methodName ="新增生产异常原因";
	        try{
	            ApiResponseResult result = workflowService.add(workflow);
	            logger.debug("新增流程信息=add:");
	            getSysLogService().success(module,method, methodName, workflow.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("新增流程信息失败！", e);
	            getSysLogService().error(module,method, methodName, workflow.toString()+";"+e.toString());
	            return ApiResponseResult.failure("新增流程信息失败！");
	        }
	    }

	    @ApiOperation(value = "编辑流程信息", notes = "编辑流程信息", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Workflow workflow){
	        String method = "check/Workflow/edit";String methodName ="编辑生产异常原因";
	        try{
	            ApiResponseResult result = workflowService.edit(workflow);
	            logger.debug("编辑流程信息=edit:");
	            getSysLogService().success(module,method, methodName, workflow.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑流程信息失败！", e);
	            getSysLogService().error(module,method, methodName, workflow.toString()+";"+e.toString());
	            return ApiResponseResult.failure("编辑流程信息失败！");
	        }
	    }



		@ApiOperation(value = "删除流程信息", notes = "删除流程信息", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "check/Workflow/delete";String methodName ="删除生产异常原因";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = workflowService.delete(id);
	            logger.debug("删除流程信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除流程信息失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("删除流程信息失败！");
	        }
	    }

	@ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
	@RequestMapping(value = "/doStatus", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
		String method = "check/Workflow/doStatus";String methodName ="设置正常/禁用";
		long id = Long.parseLong(params.get("id").toString()) ;
		Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
		try{

			ApiResponseResult result = workflowService.doStatus(id, bsStatus);
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
