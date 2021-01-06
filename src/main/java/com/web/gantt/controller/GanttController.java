package com.web.gantt.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import com.web.quote.service.OutService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "甘特图")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "gantt")
public class GanttController extends WebController {
	
	@ApiOperation(value = "甘特图", notes = "甘特图", hidden = true)
	@RequestMapping(value = "/toGantt")
	public ModelAndView toGantt() {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("/web/gantt/gantt_list2");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("甘特图失败！", e);
		}
		return mav;
	}

	@ApiOperation(value = "甘特图", notes = "甘特图", hidden = true)
	@RequestMapping(value = "/toUnGantt")
	public ModelAndView toUnGantt() {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("/web/gantt/gantt_list_john");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("甘特图失败！", e);
		}
		return mav;
	}

}
