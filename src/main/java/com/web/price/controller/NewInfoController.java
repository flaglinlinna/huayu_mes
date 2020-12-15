package com.web.price.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("报价信息创建")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "price/new_info")
public class NewInfoController extends WebController {

	//private String module = "报价信息创建";

	@ApiOperation(value = "报价信息创建页", notes = "报价信息创建页", hidden = true)
	@RequestMapping(value = "/toNewInfo")
	public String toNewInfo() {
		return "/web/price/new_info/new_info";
	}
}
