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
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取上线人员列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
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
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取上线人员记录失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
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
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取上线人员记录信息失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取上线人员记录信息失败！");
	        }
	    }
}
