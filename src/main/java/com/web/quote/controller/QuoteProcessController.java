package com.web.quote.controller;

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
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.QuoteProcessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "报价工艺流程模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteProcess")
public class QuoteProcessController extends WebController {

	private String module = "报价工艺流程";

	@Autowired
	private QuoteProcessService quoteProcessService;

	@ApiOperation(value = "报价工艺流程表结构", notes = "报价工艺流程表结构" + QuoteProcess.TABLE_NAME)
	@RequestMapping(value = "/getQuoteProcess", method = RequestMethod.GET)
	@ResponseBody
	public QuoteProcess getQuoteProcess() {
		return new QuoteProcess();
	}

	
	@ApiOperation(value = "报价工艺流程页", notes = "报价工艺流程页", hidden = true)
	@RequestMapping(value = "/toQuoteProcess")
	public ModelAndView toQuoteProcess(String quoteId,String code) {
		ModelAndView mav = new ModelAndView();
		try {
			ApiResponseResult bomNameList=quoteProcessService.getBomList2(quoteId);
			mav.addObject("quoteId", quoteId);
			mav.addObject("code", code);
			mav.addObject("bomNameList", bomNameList);
			mav.setViewName("/web/quote/01business/quote_process");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程页数据失败！", e);
		}
		return mav;
	}
	
	@ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword,String pkQuote) {
        String method = "quoteProcess/getList";String methodName ="获取报价工艺流程列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteProcessService.getList(keyword,pkQuote, super.getPageRequest(sort));
            logger.debug("获取报价工艺流程列表=getList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价工艺流程列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取报价工艺流程列表失败！");
        }
    }	
	@ApiOperation(value = "获取报价工艺流程-bom列表", notes = "获取报价工艺流程-bom列表",hidden = true)
    @RequestMapping(value = "/getBomList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getBomList(String keyword) {
        String method = "quoteProcess/getBomList";String methodName ="获取报价工艺流程-bom列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = quoteProcessService.getBomList(keyword, super.getPageRequest(sort));
            logger.debug("获取报价工艺流程列表=getBomList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价工艺流程-bom列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取报价工艺流程-bom列表失败！");
        }
    }	
	@ApiOperation(value = "获取报价工艺流程-工序列表", notes = "获取报价工艺流程-工序列表",hidden = true)
    @RequestMapping(value = "/getAddList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getAddList() {
        String method = "quoteProcess/getAddList";String methodName ="获取报价工艺流程-工序列表";
        try {
            ApiResponseResult result = quoteProcessService.getAddList();
            logger.debug("获取报价工艺流程列表=getAddList:");
            getSysLogService().success(module,method, methodName,"" );
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价工艺流程-工序列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:;"+e.toString());
            return ApiResponseResult.failure("获取报价工艺流程-工序列表失败！");
        }
    }	
	
	@ApiOperation(value = "新增报价-工艺流程", notes = "新增报价-工艺流程",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody Map<String, Object> params) {   	
        String method = "quoteProcess/add";String methodName ="新增报价-工艺流程";
        String proc = params.get("proc").toString();
        String itemId = params.get("itemId").toString();
        String quoteId = params.get("quoteId").toString();
        try{
            ApiResponseResult result = quoteProcessService.add(proc,itemId,quoteId);
            logger.debug("新增报价-工艺流程=add:");
            getSysLogService().success(module,method, methodName,
                    "外购清单:"+itemId+";报价单id:"+quoteId+";工序Id:"+proc);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增报价-工艺流程失败！", e);
            getSysLogService().error(module,method, methodName,"外购清单id:"+itemId+";报价单id:"+quoteId+";工序Id:"+proc+";"+ e.toString());
            return ApiResponseResult.failure("新增报价-工艺流程失败！");
        }
    }
	
	@ApiOperation(value = "修改顺序", notes = "修改顺序", hidden = true)
    @RequestMapping(value = "/doProcOrder", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doProcOrder(@RequestBody Map<String, Object> params) throws Exception{
        String method = "quoteProcess/doProcOrder";String methodName ="修改顺序";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
        	Integer procOrder=Integer.parseInt(params.get("procOrder").toString());
            ApiResponseResult result = quoteProcessService.doProcOrder(id, procOrder);
            logger.debug("修改顺序=doProcOrder:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改顺序失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("修改顺序失败！");
        }
    }
	
	@ApiOperation(value = "修改备注", notes = "修改备注", hidden = true)
    @RequestMapping(value = "/doFmemo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doFmemo(@RequestBody Map<String, Object> params) throws Exception{
        String method = "quoteProcess/doFmemo";String methodName ="修改备注";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
        	String fmemo=params.get("fmemo").toString();
            ApiResponseResult result = quoteProcessService.doFmemo(id, fmemo);
            logger.debug("修改备注=doFmemo:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改备注失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("修改备注失败！");
        }
    }
	
	@ApiOperation(value = "删除", notes = "删除",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "quoteProcess/delete";String methodName ="删除";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = quoteProcessService.delete(id);
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
	
	@ApiOperation(value = "提交报价-工艺流程", notes = "提交报价-工艺流程",hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> param) {   	
        String method = "quoteProcess/doStatus";String methodName ="提交报价-工艺流程";
        String pkQuote = param.get("quoteId").toString();
        String code = param.get("code").toString();
        try{
            ApiResponseResult result = quoteProcessService.doStatus(pkQuote,code);
            logger.debug("提交报价-工艺流程=doStatus:");
            getSysLogService().success(module,method, methodName,
                    "报价单id:"+pkQuote);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("提交报价-工艺流程失败！", e);
            getSysLogService().error(module,method, methodName,"报价单id:"+pkQuote+ e.toString());
            return ApiResponseResult.failure("提交报价-工艺流程失败！");
        }
    }
}
