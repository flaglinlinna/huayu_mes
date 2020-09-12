package com.web.po.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.po.service.PoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

@Api(description = "PO模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/api/po")
public class POController extends WebController {

	 @Autowired
		private PoService poService;

    @ApiOperation(value = "PO页", notes = "PO页", hidden = true)
    @RequestMapping(value = "/toPO")
    public String toPO(){
        return "/api/po/po_index";
    }
//    @ApiOperation(value = "PO列表页", notes = "PO列表页", hidden = true)
//    @RequestMapping(value = "/toPOList")
//    public String toPOList(){
//        return "/api/po/po_list";
//    }
    @ApiOperation(value = "PO列表页", notes = "PO列表页", hidden = true)
    @RequestMapping(value = "/toPOList")
    @ResponseBody
    public ModelAndView toPOList(String type){
        ModelAndView mav = new ModelAndView("/api/po/po_list");
        mav.addObject("type", type);
        return mav;
    }
	
    @PostMapping(value = "/board")
    @ApiOperation(value = "获取PO信息")
    @ResponseBody
    public ApiResponseResult findProductionPOBoardData(){
        try {
			return poService.findProductionPOBoardData("findProductionPOBoardData");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ApiResponseResult.failure(e.toString());
		}
    }
    
    @PostMapping(value = "/polist")
    @ApiOperation(value = "获取PO_List信息")
    @ResponseBody
    public ApiResponseResult findPoLineList(@RequestParam(value = "type", required = false) String type,
    		@RequestParam(value = "page", required = false) int page,@RequestParam(value = "rows", required = false) int rows){
        try {
			return poService.findPoLineList(type, page, rows);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ApiResponseResult.failure(e.toString());
		}
    }
    
}
