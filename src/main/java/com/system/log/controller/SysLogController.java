package com.system.log.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.log.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(description = "日志管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysLog")
public class SysLogController extends WebController {

    @Autowired
    private SysLogService sysLogService;

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", dataType = "String", paramType = "query", defaultValue = "")
    })
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getlist(String keyword){
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            return sysLogService.getlist(keyword, super.getPageRequest(sort));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponseResult.failure("获取日志列表失败！");
        }
    }
    
    @RequestMapping(value = "/toLog")
    public String toUserList(){
        return "/system/log/log";
    }

}
