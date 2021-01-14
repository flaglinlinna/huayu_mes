package com.web.quote.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteMould;
import com.web.quote.service.QuoteMouldService;
import com.web.quote.service.QuoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "模具清单维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteMould")
public class QuoteMouldController extends WebController {

	private String module = "模具清单维护";

	@Autowired
	private QuoteMouldService quoteMouldService;
	
	@Autowired
	private QuoteService quoteService;

	@ApiOperation(value = "模具清单维护表结构", notes = "模具清单维护表结构" + QuoteMould.TABLE_NAME)
	@RequestMapping(value = "/getQuoteMould", method = RequestMethod.GET)
	@ResponseBody
	public QuoteMould getQuoteMould() {
		return new QuoteMould();
	}

	
	@ApiOperation(value = "模具清单维护页", notes = "模具清单维护页", hidden = true)
	@RequestMapping(value = "/toQuoteMould")
	public ModelAndView toQuoteMould(String quoteId,String code) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult bomNameList=quoteMouldService.getBomList(quoteId);
			ApiResponseResult iStatus =quoteService.getItemStatus(Long.parseLong(quoteId),code);
			mav.addObject("quoteId", quoteId);
			mav.addObject("code", code);
			mav.addObject("nowStatus", iStatus);
			mav.addObject("bomNameList", bomNameList);
			mav.setViewName("/web/quote/01business/quote_mould");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取模具清单维护页数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "获取模具清单维护列表", notes = "获取模具清单维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword,String pkQuote) {
        String method = "quoteMould/getList";String methodName ="获取模具清单维护列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteMouldService.getList(keyword,pkQuote, super.getPageRequest(sort));
            logger.debug("获取模具清单维护列表=getList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取模具清单维护列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取模具清单维护列表失败！");
        }
    }	
	
	@ApiOperation(value = "获取模具清单维护-模具成本列表", notes = "获取模具清单维护-模具成本列表",hidden = true)
    @RequestMapping(value = "/getMouldList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getMouldList() {
        String method = "quoteMould/getMouldList";String methodName ="获取模具清单维护-模具成本列表";
        try {
            ApiResponseResult result = quoteMouldService.getMouldList();
            logger.debug("获取模具清单维护列表=getMouldList:");
            getSysLogService().success(module,method, methodName,"" );
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取模具清单维护-模具成本列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:;"+e.toString());
            return ApiResponseResult.failure("获取模具清单维护-模具成本列表失败！");
        }
    }	
	
	@ApiOperation(value = "新增报价-模具清单", notes = "新增报价-模具清单",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody Map<String, Object> params) {   	
        String method = "quoteMould/add";String methodName ="新增报价-模具清单";
        String proc = params.get("proc").toString();
        String itemId = params.get("itemId").toString();
        String quoteId = params.get("quoteId").toString();
        try{
            ApiResponseResult result = quoteMouldService.add(proc,itemId,quoteId);
            logger.debug("新增报价-模具清单=add:");
            getSysLogService().success(module,method, methodName,
                    "外购清单:"+itemId+";报价单id:"+quoteId+";工序Id:"+proc);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增报价-模具清单失败！", e);
            getSysLogService().error(module,method, methodName,"外购清单id:"+itemId+";报价单id:"+quoteId+";工序Id:"+proc+";"+ e.toString());
            return ApiResponseResult.failure("新增报价-模具清单失败！");
        }
    }
	
	@ApiOperation(value = "修改实际报价", notes = "修改实际报价", hidden = true)
    @RequestMapping(value = "/doActQuote", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doActQuote(@RequestBody Map<String, Object> params) throws Exception{
        String method = "quoteMould/doActQuote";String methodName ="修改实际报价";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
        	BigDecimal actQuote=new BigDecimal(params.get("actQuote").toString()) ;
            ApiResponseResult result = quoteMouldService.doActQuote(id, actQuote);
            logger.debug("修改实际报价=doActQuote:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改实际报价失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("修改实际报价失败！");
        }
    }
	
	@ApiOperation(value = "删除", notes = "删除",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "quoteMould/delete";String methodName ="删除";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = quoteMouldService.delete(id);
            logger.debug("删除=delete:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("删除失败！");
        }
    }
	
	@ApiOperation(value = "提交报价-模具清单", notes = "提交报价-模具清单",hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> param) {   	
        String method = "quoteMould/doStatus";String methodName ="提交报价-模具清单";
        String pkQuote = param.get("quoteId").toString();
        String code = param.get("code").toString();
        try{
            ApiResponseResult result = quoteMouldService.doStatus(pkQuote,code);
            logger.debug("提交报价-模具清单=doStatus:");
            getSysLogService().success(module,method, methodName,
                    "报价单id:"+pkQuote);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("提交报价-模具清单失败！", e);
            getSysLogService().error(module,method, methodName,"报价单id:"+pkQuote+ e.toString());
            return ApiResponseResult.failure("提交报价-模具清单失败！");
        }
    }
	
	@ApiOperation(value = "设置-不需要报价状态", notes = "设置-不需要报价状态",hidden = true)
    @RequestMapping(value = "/doNoNeed", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doNoNeed(@RequestBody Map<String, Object> param) {   	
        String method = "quoteMould/doNoNeed";String methodName ="设置-不需要报价状态";
        String pkQuote = param.get("quoteId").toString();
        String code = param.get("code").toString();
        try{
            ApiResponseResult result = quoteMouldService.doNoNeed(pkQuote,code);
            logger.debug("设置-不需要报价状态=doNoNeed:");
            getSysLogService().success(module,method, methodName,
                    "报价单id:"+pkQuote);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("设置-不需要报价状态失败！", e);
            getSysLogService().error(module,method, methodName,"报价单id:"+pkQuote+ e.toString());
            return ApiResponseResult.failure("设置-不需要报价状态失败！");
        }
    }
}
