package com.system.session.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.session.service.MySessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "日志管理模块")
@CrossOrigin
@ControllerAdvice
@RestController
@RequestMapping(value = "/session")
public class MySessionController extends WebController {

    @Autowired
    private MySessionService mySessionService;

    @ApiOperation(value = "获取在线用户列表", notes = "获取在线用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", dataType = "String", paramType = "query", defaultValue = "")
    })
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public ApiResponseResult getlist(String keyword){
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            return mySessionService.getlist(keyword, super.getPageRequest(sort));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponseResult.failure("获取日志列表失败！");
        }
    }

}
