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
import com.web.produce.entity.OnlineStaff;
import com.web.produce.entity.DevClock;
import com.web.produce.service.OnlineStaffService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "上线人员模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "produce/online")
public class OnlineStaffController extends WebController{

	private String module = "上线人员信息";
	 @Autowired
	 private OnlineStaffService onlineStaffService;
	 
	 @ApiOperation(value = "上线人员表结构", notes = "上线人员表结构"+OnlineStaff.TABLE_NAME)
	    @RequestMapping(value = "/getOnlineStaff", method = RequestMethod.GET)
		@ResponseBody
	    public OnlineStaff getOnlineStaff(){
	        return new OnlineStaff();
	    }
	  
	 @ApiOperation(value = "上线人员列表页", notes = "上线人员列表页", hidden = true)
	    @RequestMapping(value = "/toOnlineStaff")
	    public String toOnlineStaff(){
	        return "/web/produce/dev_clock/online";
	    }
	 
	    @ApiOperation(value = "获取上线人员列表", notes = "获取上线人员列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "produce/online/getList";String methodName ="获取上线人员列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = onlineStaffService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取上线人员列表=getList:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取上线人员列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取上线人员列表失败！");
	        }
	    }
	    
	    @ApiOperation(value = "根据ID获取上线人员记录", notes = "根据ID获取上线人员记录",hidden = true)
	    @RequestMapping(value = "/getMain", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getMain(@RequestBody Map<String, Object> params){
	        String method = "produce/online/getMain";String methodName ="根据ID获取上线人员记录";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = onlineStaffService.getMain(id);
	            logger.debug("根据ID获取上线人员记录=getMain:");
//	            getSysLogService().success(module,method, methodName, "id:"+id);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取上线人员记录失败！", e);
	            getSysLogService().error(module,method, methodName, "id:"+id+e.toString());
	            return ApiResponseResult.failure("获取上线人员记录失败！");
	        }
	    }
	    
	    @ApiOperation(value = "根据ID获取上线人员记录信息", notes = "根据ID获取上线人员记录",hidden = true)
	    @RequestMapping(value = "/getMainInfo", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getMainInfo(@RequestBody Map<String, Object> params){
	        String method = "produce/online/getMainInfo";String methodName ="根据ID获取上线人员记录信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = onlineStaffService.getMainInfo(id);
	            logger.debug("根据ID获取上线人员记录信息=getMainInfo:");
//	            getSysLogService().success(module,method, methodName, "id:"+id);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取上线人员记录信息失败！", e);
	            getSysLogService().error(module,method, methodName, "id:"+id+e.toString());
	            return ApiResponseResult.failure("获取上线人员记录信息失败！");
	        }
	    }
	    
	    @ApiOperation(value = "获取班次信息", notes = "获取班次",hidden = true)
	    @RequestMapping(value = "/getClassList", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getClassList(){
	        String method = "produce/online/getClassList";String methodName ="获取班次信息";
	        try{
	            ApiResponseResult result = onlineStaffService.getClassList();
	            logger.debug("获取班次信息=getClassList:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("获取班次信息失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取班次信息失败！");
	        }
	    }
	    
	    @ApiOperation(value = "修改上线人员主表", notes = "修改上线人员主表",hidden = true)
	    @RequestMapping(value = "/editMain", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult editMain(@RequestBody OnlineStaff onlineStaff){
	        String method = "produce/online/editMain";String methodName ="修改上线人员主表";
	        try{
	            ApiResponseResult result = onlineStaffService.editMain(onlineStaff);
	            logger.debug("修改上线人员主表=editMain:");
	            getSysLogService().success(module,method, methodName, onlineStaff.toString());
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("修改上线人员主表失败！", e);
	            getSysLogService().error(module,method, methodName, onlineStaff.toString()+e.toString());
	            return ApiResponseResult.failure("修改上线人员主表失败！");
	        }
	    }
	    
	    @ApiOperation(value = "删除副表记录", notes = "删除副表记录",hidden = true)
	    @RequestMapping(value = "/deleteVice", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult deleteVice(@RequestBody Map<String, Object> params){
	        String method = "produce/online/deleteVice";String methodName ="删除副表记录";
	        try{

	        	String taskNo=params.get("taskNo")==null?"":params.get("taskNo").toString();
	        	String devId=params.get("devId")==null?"":params.get("devId").toString();
	        	String empId=params.get("empId")==null?"":params.get("empId").toString();
	        	String viceId=params.get("viceId")==null?"":params.get("viceId").toString();
	        	String beginTime=params.get("beginTime")==null?"":params.get("beginTime").toString();
				String logInfo = "指令单号:"+taskNo+";设备ID:"+devId+";员工ID"+empId+";从表id"+devId+";上线时间"+beginTime;
	            ApiResponseResult result = onlineStaffService.deleteVice(taskNo,devId, empId,viceId,beginTime);
	            logger.debug("删除副表记录=deleteVice:");
	            getSysLogService().success(module,method, methodName, logInfo);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("删除副表记录失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("删除副表主表失败！");
	        }
	    }
	    
	    @ApiOperation(value = "删除上线人员记录信息", notes = "删除上线人员记录",hidden = true)
	    @RequestMapping(value = "/deleteMain", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult deleteMain(@RequestBody Map<String, Object> params){
	        String method = "produce/online/deleteMain";String methodName ="删除上线人员记录信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = onlineStaffService.deleteMain(id);
	            logger.debug("删除上线人员记录信息=deleteMain:");
	            getSysLogService().success(module,method, methodName, "id:"+id);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("删除上线人员记录信息失败！", e);
	            getSysLogService().error(module,method, methodName,"id:"+id+ e.toString());
	            return ApiResponseResult.failure("获取上线人员记录信息失败！");
	        }
	    }
	    
}
