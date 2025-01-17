package com.web.quote.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.utils.TypeChangeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.QuoteProcessService;
import com.web.quote.service.QuoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(description = "报价工艺流程模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "quoteProcess")
public class QuoteProcessController extends WebController {

	private String module = "报价工艺流程";

	@Autowired
	private QuoteProcessService quoteProcessService;
	
	@Autowired
	private QuoteService quoteService;

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
			ApiResponseResult iStatus =quoteService.getStatus(Long.parseLong(quoteId));
			mav.addObject("quoteId", quoteId);
			mav.addObject("code", code);
			mav.addObject("nowStatus", iStatus.getData());
			mav.addObject("bomNameList", bomNameList);
			mav.setViewName("/web/quote/01business/quote_process");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报价工艺流程页数据失败！", e);
		}
		return mav;
	}

    @ApiOperation(value = "获取工艺零件名称列表", notes = "获取工艺零件名称列表",hidden = true)
    @RequestMapping(value = "/getBomNameList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getBomNameList(String keyword,String pkQuote) {
        String method = "quoteProcess/getBomNameList";String methodName ="获取工艺零件名称列表";
        try {
            Sort sort = Sort.unsorted();
            ApiResponseResult result = quoteProcessService.getList(keyword,pkQuote, super.getPageRequest(sort));
            logger.debug("获取报价工艺流程列表=getList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺零件名称列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取工艺零件名称列表失败！");
        }
    }


    @ApiOperation(value = "获取报价工艺流程列表", notes = "获取报价工艺流程列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword,String pkQuote) {
        String method = "quoteProcess/getList";String methodName ="获取报价工艺流程列表";
        try {
            //Sort sort = new Sort(Sort.Direction.ASC, "id");
//        	 Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "bsName");
        	 Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "BsOrder");
        	 List<Sort.Order> list = new ArrayList<>();
//        	 list.add(order1);
        	 list.add(order2);
        	 Sort sort = new Sort(list);
            ApiResponseResult result = quoteProcessService.getList(keyword,pkQuote, super.getPageRequest(sort));
            logger.debug("获取报价工艺流程列表=getList:");
            //getSysLogService().success(module,method, methodName, keyword);
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
    public ApiResponseResult getBomList(String keyword,String quoteId) {
        String method = "quoteProcess/getBomList";String methodName ="获取报价工艺流程-bom列表";
        try {
            Sort sort =  Sort.unsorted();
            ApiResponseResult result = quoteProcessService.getBomList(keyword,Long.parseLong(quoteId), super.getPageRequest(sort));
            logger.debug("获取报价工艺流程列表=getBomList:");
            //getSysLogService().success(module,method, methodName, keyword);
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
    public ApiResponseResult getAddList(Long pkWcId) {
        String method = "quoteProcess/getAddList";String methodName ="获取报价工艺流程-工序列表";
        try {
            Sort sort =  Sort.unsorted();
            ApiResponseResult result = quoteProcessService.getAddList(pkWcId,super.getPageRequest(sort));
            logger.debug("获取报价工艺流程列表=getAddList:");
            //getSysLogService().success(module,method, methodName,"" );
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
        String bsElement = params.get("bsElement").toString();
        String quoteId = params.get("quoteId").toString();
        String bsBomId = params.get("bsBomId").toString();
        try{
            ApiResponseResult result = quoteProcessService.add(proc,itemId,quoteId,bsElement,bsBomId);
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

    @ApiOperation(value = "修改材料名称", notes = "修改材料名称", hidden = true)
    @RequestMapping(value = "/doBsMaterName", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doBsMaterName(@RequestBody Map<String, Object> params) throws Exception{
        String method = "quoteProcess/doBsMaterName";String methodName ="修改材料名称";
        try{
            Long id = Long.parseLong(params.get("id").toString()) ;
            String bomIds = params.get("bomId").toString();
            Long bomId =StringUtils.isNotEmpty(bomIds)?Long.parseLong(bomIds):null;
            ApiResponseResult result = quoteProcessService.doBsMaterName(id, bomId);
            logger.debug("修改材料名称=doBsMaterName:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改材料名称失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("修改材料名称失败！");
        }
    }

    @ApiOperation(value = "修改损耗分组", notes = "修改损耗分组", hidden = true)
    @RequestMapping(value = "/doBsGroups", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doBsGroups(@RequestBody Map<String, Object> params) throws Exception{
        String method = "quoteProcess/doBsGroups";String methodName ="修改损耗分组";
        try{
            Long id = Long.parseLong(params.get("id").toString()) ;
            String bsGroups = params.get("bsGroups").toString();
            ApiResponseResult result = quoteProcessService.doBsGroups(id, bsGroups);
            logger.debug("修改损耗分组=doBsGroups:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改损耗分组失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("修改损耗分组失败！");
        }
    }

    @ApiOperation(value = "修改工艺", notes = "修改工艺", hidden = true)
    @RequestMapping(value = "/doProc", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doProc(@RequestBody Map<String, Object> params) throws Exception{
        String method = "quoteProcess/doProc";String methodName ="修改工艺";
        try{
            Long id = Long.parseLong(params.get("id").toString()) ;
            Long bsGroups = params.get("prodId")==null?null:Long.parseLong(params.get("prodId").toString());
            ApiResponseResult result = quoteProcessService.doProc(id, bsGroups);
            logger.debug("修改工艺=doProc:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改工艺失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("修改工艺失败！");
        }
    }
	
	@ApiOperation(value = "删除", notes = "删除",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "quoteProcess/delete";String methodName ="删除";
        try{
        	String ids = params.get("id").toString() ;
            ApiResponseResult result = quoteProcessService.delete(ids);
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
//        String again = param.get("again").toString();
        List<QuoteProcess> quoteProcessList = TypeChangeUtils.objectToList(param.get("dates"),QuoteProcess.class);
        try{
            ApiResponseResult result = quoteProcessService.doStatus(pkQuote,code,quoteProcessList);
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

    @ApiOperation(value = "取消提交-工艺流程", notes = "取消提交-工艺流程",hidden = true)
    @RequestMapping(value = "/cancelStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult cancelStatus(@RequestBody Map<String, Object> param) {
        String method = "quoteProcess/cancelStatus";String methodName ="取消提交-工艺流程";
        String pkQuote = param.get("quoteId").toString();
        String code = param.get("code").toString();
        try{
            ApiResponseResult result = quoteProcessService.cancelStatus(pkQuote,code);
            logger.debug("取消提交-工艺流程=doStatus:");
            getSysLogService().success(module,method, methodName,
                    "报价单id:"+pkQuote);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("取消提交-工艺流程失败！", e);
            getSysLogService().error(module,method, methodName,"报价单id:"+pkQuote+ e.toString());
            return ApiResponseResult.failure("取消提交-工艺流程失败！");
        }
    }

    @ApiOperation(value = "页面编辑保存", notes = "页面编辑保存",hidden = true)
    @RequestMapping(value = "/saveTable", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult saveTable(@RequestBody List<QuoteProcess> quoteProcessList) {
        String method = "quoteProcess/saveTable";String methodName ="页面编辑保存";
        try{
            ApiResponseResult result = quoteProcessService.editProcessList(quoteProcessList);
            logger.debug("页面编辑保存=saveTable:");
            getSysLogService().success(module,method, methodName,
                    "");
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("页面编辑保存失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("页面编辑保存失败！");
        }
    }

    @ApiOperation(value = "页面编辑保存", notes = "页面编辑保存",hidden = true)
    @RequestMapping(value = "/saveTableAgain", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult saveTableAgain(@RequestBody List<QuoteProcess> quoteProcessList) {
        String method = "quoteProcess/saveTableAgain";String methodName ="页面编辑保存";
        try{
            ApiResponseResult result = quoteProcessService.editProcessListAgain(quoteProcessList);
            logger.debug("页面编辑保存=saveTable:");
            getSysLogService().success(module,method, methodName,
                    "");
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("页面编辑保存失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("页面编辑保存失败！");
        }
    }
	
	@ApiOperation(value = "获取报价工艺流程-工序列表", notes = "获取报价工艺流程-工序列表",hidden = true)
    @RequestMapping(value = "/getListByQuoteAndName", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getListByQuoteAndName(String quoteId,String name) {
        String method = "quoteProcess/getListByQuoteAndName";String methodName ="获取报价工艺流程-工序列表";
        try {
            ApiResponseResult result = quoteProcessService.getListByQuoteAndName(quoteId, name);
            logger.debug("获取报价工艺流程列表=getAddList:");
//            getSysLogService().success(module,method, methodName,"" );
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取报价工艺流程-工序列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:;"+e.toString());
            return ApiResponseResult.failure("获取报价工艺流程-工序列表失败！");
        }
    }

    @ApiOperation(value = "获取损耗明细模拟列表", notes = "获取损耗明细模拟列表", hidden = true)
    @RequestMapping(value = "/getSumList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getSumList(String quoteId) {
        String method = "/quoteProcess/getSumList";
        String methodName = "获取损耗明细模拟列表";
        try {
            ApiResponseResult result = quoteProcessService.getSumList(Long.parseLong(quoteId),super.getPageRequest(Sort.unsorted()));
            logger.debug("获取损耗明细模拟列表=getList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取损耗明细模拟列表失败！", e);
			getSysLogService().error(module,method, methodName,e.toString());
            return ApiResponseResult.failure("获取损耗明细模拟列表失败！");
        }
    }

    @ApiOperation(value="导出数据", notes="导出数据", hidden = true)
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    @ResponseBody
    public void exportExcel(HttpServletResponse response, String bsType, Long pkQuote) {
        String method = "/productProcess/exportExcel";String methodName ="导出数据";
        try {
            logger.debug("导出数据=exportExcel:");
            getSysLogService().success(module,method, methodName, "");
            quoteProcessService.exportExcel(response,bsType,pkQuote);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出数据失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
        }
    }
}
