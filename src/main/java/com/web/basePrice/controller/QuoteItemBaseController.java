package com.web.basePrice.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.service.QuoteItemBaseService;
import com.web.quote.entity.QuoteItemBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Api(description = "报价制造信息模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/quoteItemBase")
public class QuoteItemBaseController extends WebController {

	private String module = "报价项目设置模块";

	@Autowired
	private QuoteItemBaseService quoteItemBaseService;

	@ApiOperation(value = "报价项目设置列表页", notes = "报价项目设置列表页", hidden = true)
	@RequestMapping(value = "/toQuoteItemBase")
	public ModelAndView toQuoteItemBase() {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("/web/basePrice/quote_item_base");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价基础数据失败！", e);
		}
		return mav;
	}

	
	@ApiOperation(value = "获取报价项目设置列表", notes = "获取报价项目设置列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "quote/getList";String methodName ="获取报价项目设置列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteItemBaseService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取报价项目设置列表=getList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价项目设置失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取报价项目设置列表失败！");
        }
    }

    @ApiOperation(value = "编辑报价项目设置", notes = "编辑报价项目设置",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody QuoteItemBase quoteItemBase){
        String method = "basePrice/quoteItemBase/edit";String methodName ="编辑报价项目设置";
        try{
            ApiResponseResult result = quoteItemBaseService.edit(quoteItemBase);
            logger.debug("编辑工序信息维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑工序信息维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑工序信息维护失败！");
        }
    }

}
