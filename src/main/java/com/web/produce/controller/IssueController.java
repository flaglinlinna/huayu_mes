package com.web.produce.controller;

import java.util.Date;
import java.util.Map;

import org.apache.shiro.env.Environment;
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
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.Issue;
import com.web.produce.entity.DevClock;
import com.web.produce.service.IssueService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "指纹下发记录模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "produce/issue")
public class IssueController extends WebController{

	 @Autowired
	 private IssueService issueService;
	 
	 
	 @ApiOperation(value = "指纹下发记录列表页", notes = "指纹下发记录列表页", hidden = true)
	 @RequestMapping(value = "/toIssue")
	 public ModelAndView toIssue(String ptype) {
			ModelAndView mav=new ModelAndView();
			mav.addObject("ptype", ptype);
			mav.setViewName("/web/produce/dev_clock/issue");//返回路径
			return mav;
		}

	 
	 @ApiOperation(value = "指纹下发记录列表页", notes = "指纹下发记录列表页", hidden = true)
	    @RequestMapping(value = "/toClear")
	    public String toClear(){
	        return "/web/produce/dev_clock/clear";
	    }
	 
	 @ApiOperation(value = "指纹下发记录表结构", notes = "指纹下发记录表结构"+Issue.TABLE_NAME)
	    @RequestMapping(value = "/getIssue", method = RequestMethod.GET)
		@ResponseBody
	    public Issue getIssue(){
	        return new Issue();
	    }
	  
	 
	 
	    @ApiOperation(value = "获取指纹下发记录列表", notes = "获取指纹下发记录列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword,String ptype) {
	        String method = "produce/issue/getList";String methodName ="获取指纹下发记录列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = issueService.getList(keyword,ptype, super.getPageRequest(sort));
	            logger.debug("获取指纹下发记录列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取指纹下发记录列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取指纹下发记录列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增下发记录", notes = "新增下发记录",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Map<String, Object> params) {
	        String method = "produce/issue/add";String methodName ="新增下发记录";
	        try{
	        	
	        	String devList = params.get("devList").toString();
	        	String empList = params.get("empList").toString();
	            ApiResponseResult result = issueService.add(devList,empList);
	            logger.debug("新增下发记录=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("下发记录新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("下发记录新增失败！");
	        }
	    }
	    @ApiOperation(value = "根据ID获取下发记录", notes = "根据ID获取下发记录",hidden = true)
	    @RequestMapping(value = "/getIssue", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getIssue(@RequestBody Map<String, Object> params){
	        String method = "produce/issue/getIssue";String methodName ="根据ID获取下发记录";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = issueService.getIssue(id);
	            logger.debug("根据ID获取下发记录=getIssue:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取下发记录失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取下发记录失败！");
	        }
	    }
		
		@ApiOperation(value = "删除下发记录", notes = "删除下发记录",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "produce/issue/delete";String methodName ="删除下发记录";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = issueService.delete(id);
	            logger.debug("删除下发记录=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除下发记录失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除下发记录失败！");
	        }
	    }
		
		@ApiOperation(value = "获取人员信息列表", notes = "获取人员信息列表", hidden = true)
	    @RequestMapping(value = "/getEmp", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getEmp(String empKeyword) {
	        String method = "produce/issue/getEmp";String methodName ="获取人员信息列表";
	        try {
	        	Sort sort = new Sort(Sort.Direction.DESC, "emp_id");
	            ApiResponseResult result = issueService.getEmp(empKeyword, super.getPageRequest(sort));
	            logger.debug("获取人员信息列表=getEmp:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取人员信息列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取人员信息列表失败！");
	        }
	    }
		@ApiOperation(value = "获取卡机信息列表", notes = "获取卡机信息列表", hidden = true)
	    @RequestMapping(value = "/getDev", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getDev(String devKeyword) {
	        String method = "produce/issue/getDev";String methodName ="获取卡机信息列表";
	        try {
	        	Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = issueService.getDev(devKeyword, super.getPageRequest(sort));
	            logger.debug("获取卡机信息列表=getDev:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取卡机信息列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取卡机信息列表失败！");
	        }
	    }
		
		 @ApiOperation(value = "删除下发记录", notes = "删除下发记录",hidden = true)
		    @RequestMapping(value = "/clear", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult clear(@RequestBody Map<String, Object> params) {
		        String method = "produce/issue/clear";String methodName ="删除下发记录";
		        try{
		        	String devList = params.get("devList").toString();
		        	String empList = params.get("empList").toString();
		            ApiResponseResult result = issueService.clear(devList,empList);
		            logger.debug("删除下发记录=add:");
		            getSysLogService().success(method, methodName, null);
		            return result;
		        }catch(Exception e){
		            e.printStackTrace();
		            logger.error("删除记录失败！", e);
		            getSysLogService().error(method, methodName, e.toString());
		            return ApiResponseResult.failure("删除记录失败！");
		        }
		    }
}
