package com.system.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.base.control.WebController;

import io.swagger.annotations.Api;
import org.springframework.web.servlet.ModelAndView;

@Api(description = "首页内容展示模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/console")
public class ConsoleController extends WebController {

    
    @RequestMapping(value = "/toConsole")
    public String toUserList(){
        return "/home/console";
    }
    
    @RequestMapping(value = "/toWaitlist")
    public ModelAndView toWaitlist(String bsStatus){
        ModelAndView mv = new ModelAndView();
        mv.addObject("bsStatus",bsStatus);
        mv.setViewName("/home/waitlist");// 返回路径
        return  mv;
    }
    
    @RequestMapping(value = "/toConsole1")
    public String toUserList1(){
        return "/home/console1";
    }
    
    @RequestMapping(value = "/toSupplierInfo")
    public String toSupplierInfo(){
        return "/supplier/supplierInfo";
    }

}
