package com.system.echarts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.base.control.WebController;
import com.system.echarts.service.ChartsService;

import io.swagger.annotations.Api;

@Api(description = "报表demo")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/charts")
public class ChartsController extends WebController {

    @Autowired
    private ChartsService chartsService;
    
    @RequestMapping(value = "/toLine")
    public String toLine(){
        return "/charts/line";
    }
    
    @RequestMapping(value = "/toBar")
    public String toBar(){
        return "/charts/bar";
    }

}
