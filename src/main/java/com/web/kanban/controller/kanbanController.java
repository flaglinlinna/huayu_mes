package com.web.kanban.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.kanban.service.KanbanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "看板")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/kanban")
public class kanbanController extends WebController {

    @Autowired
    private KanbanService kanbanService;
	
	@RequestMapping(value = "/toDemo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toDemo() {
		String method = "/kanban/toDemo";String methodName ="看板demo";
		ModelAndView mav=new ModelAndView();
		//mav.addObject("pname", p);
		mav.setViewName("/kanban/demo");//返回路径
		return mav;
	}
    
}
