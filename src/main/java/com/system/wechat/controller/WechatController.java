package com.system.wechat.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.system.wechat.service.WechatSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hjj
 *
 */
@Api(description = "企业微信模块")
@RestController
@RequestMapping(value= "/wechat")
public class WechatController extends WebController {
	@Autowired
	private WechatSettingService wechatSettingService;
	

}

