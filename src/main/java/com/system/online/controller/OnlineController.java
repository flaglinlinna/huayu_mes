package com.system.online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.online.service.OnlineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "在线用户管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/online")
public class OnlineController extends WebController {

    @Autowired
    private OnlineService onlineService;

    @ApiOperation(value = "获取在线用户列表", notes = "获取在线用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", dataType = "String", paramType = "query", defaultValue = "")
    })
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getlist(String keyword){
    	
    	String method = "/online/getlist";String methodName ="获取在线用户列表";
		getSysLogService().debug(method,methodName);
		
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            return onlineService.getlist(keyword, super.getPageRequest(sort));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponseResult.failure("获取日志列表失败！");
        }
    }
    @RequestMapping(value = "/toOnline")
    public String toUserList(){
        return "/system/online/online";
    }
    
    @ApiOperation(value = "踢出用户", notes = "踢出用户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
    public ApiResponseResult delete(@RequestParam(value = "sessionId", required = false) String sessionId){
		String method = "/online/delete";String methodName ="踢出用户";
		getSysLogService().debug(method,methodName);
        try{
            return onlineService.delete(sessionId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            getSysLogService().error(method,methodName,e.toString());
            return  ApiResponseResult.failure("踢出用户操作失败，请联系管理员！");
        }
    }

}
