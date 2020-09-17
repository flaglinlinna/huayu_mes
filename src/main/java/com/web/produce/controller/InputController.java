package com.web.produce.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.SchedulingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(description = "生产投料模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "/input")
public class InputController extends WebController {

    @ApiOperation(value = "生产投料页", notes = "生产投料页", hidden = true)
    @RequestMapping(value = "/toInputAdd")
    public String toInputAdd(){
        return "/web/produce/input/input_add";
    }
    
    @ApiOperation(value = "生产投料列表页", notes = "生产投料列表页", hidden = true)
    @RequestMapping(value = "/toInputList")
    public String toInputList(){
        return "/web/produce/input/input_list";
    }

}
