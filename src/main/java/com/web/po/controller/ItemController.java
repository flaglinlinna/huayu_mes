package com.web.po.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.po.service.DatabaseService;
import com.web.po.service.ItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Item预测协同模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/api/item")
public class ItemController extends WebController {

	@Autowired
	private ItemService itemService;
	
	@ApiOperation(value = "Item页", notes = "Item页", hidden = true)
	@RequestMapping(value = "/toItemList")
	public String toPOList(){
	    return "/api/item/item_list";
	}
	
	
    
	@ApiOperation(value = "获取Item预测协同信息", notes = "获取配置列表")
    @RequestMapping(value = "/itemlist", method = RequestMethod.GET)
    @ResponseBody
	public ApiResponseResult findForecastList(@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "page", required = false) int page,@RequestParam(value = "rows", required = false) int rows){
	    try {
			return itemService.findForecastList(type, page, rows);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ApiResponseResult.failure(e.toString());
		}
	}

    
}
