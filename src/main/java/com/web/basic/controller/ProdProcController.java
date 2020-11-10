package com.web.basic.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ProdProcDetail;
import com.web.basic.service.ProdProcService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工艺流程")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/prodproc")
public class ProdProcController extends WebController{

    private String module = "工艺流程";

	@Autowired
	 private ProdProcService procProdService;
	
	 @ApiOperation(value = "工艺流程基础信息表结构", notes = "工艺流程基础信息表结构"+ProdProcDetail.TABLE_NAME)
	    @RequestMapping(value = "/getProdProc", method = RequestMethod.GET)
		@ResponseBody
	    public ProdProcDetail getProdProc(){
	        return new ProdProcDetail();
	    }
	
	@ApiOperation(value = "工艺流程列表页", notes = "工艺流程列表页", hidden = true)
    @RequestMapping(value = "/toProdProc")
    public String toProdProc(){
        return "/web/basic/prod_proc";
    }

	@ApiOperation(value = "获取工艺流程列表", notes = "获取工艺流程列表", hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "base/prodproc/getList";String methodName ="获取工艺流程列表";
        try {
        	System.out.println(keyword);
            //Sort sort = new Sort(Sort.Direction.DESC, "itemId");
            Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "itemId");
            Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "procOrder");
	       	 //Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "process.procOrder");
	       	 List<Sort.Order> list = new ArrayList<>();
	       	 list.add(order1);
	       	 list.add(order2);
	       	 Sort sort = new Sort(list);
            ApiResponseResult result = procProdService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取工艺流程列表=getList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺流程列表失败！", e);
            getSysLogService().error(module,method, methodName,keyword+":"+ e.toString());
            return ApiResponseResult.failure("获取工艺流程列表失败！");
        }
    }
	
	/*@ApiOperation(value = "获取工艺流程-工序列表", notes = "获取工艺流程-工序列表", hidden = true)
    @RequestMapping(value = "/getDetailList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getDetailList(String keyword) {
        String method = "base/prodproc/getDetailList";String methodName ="获取工艺流程-工序列表";
        try {
        	System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = procProdService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取工艺流程-工序列表=getDetailList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺流程-工序列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工艺流程-工序列表失败！");
        }
    }*/
	

	@ApiOperation(value = "获取产品列表", notes = "获取产品列表", hidden = true)
    @RequestMapping(value = "/getProdList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getProdList(String keyword ){
        String method = "base/prodproc/getProdList";String methodName ="获取产品列表";
        try{
        	Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = procProdService.getProdList(keyword, super.getPageRequest(sort));
            logger.debug("获取产品列表=getProdList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取产品列表失败！", e);
            getSysLogService().error(module,method, methodName, keyword+":"+e.toString());
            return ApiResponseResult.failure("获取产品列表失败！");
        }
    }
	
	
	@ApiOperation(value = "获取产品列表,客户列表,工序列表", notes = "获取产品列表,客户列表,工序列表", hidden = true)
    @RequestMapping(value = "/getAddList", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getAddList(){
        String method = "base/prodproc/getAddList";String methodName ="获取产品列表,客户列表,工序列表";
        try{
            ApiResponseResult result = procProdService.getAddList();
            logger.debug("获取产品列表,客户列表,工序列表=getAddList:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取产品列表,客户列表,工序列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取产品列表,客户列表,工序列表失败！");
        }
    }
	
	@ApiOperation(value = "新增", notes = "新增",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody Map<String, Object> params) {   	
        String method = "base/prodproc/add";String methodName ="新增";
        try{
        	String proc = params.get("proc").toString();
        	String itemIds = params.get("itemIds").toString();
        	String itemNos = params.get("itemNos").toString();
            ApiResponseResult result = procProdService.add(proc,itemIds,itemNos);
            logger.debug("新增=add:");
            getSysLogService().success(module,method, methodName,
                    "物料id:"+itemIds+";物料编码:"+itemNos+";工序Id:"+proc);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("新增失败！");
        }
    }
	
	@ApiOperation(value = "删除", notes = "删除",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "base/prodproc/delete";String methodName ="删除";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = procProdService.delete(id);
            logger.debug("删除=delete:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("删除！");
        }
    }
	
	@ApiOperation(value = "设置过程属性", notes = "设置过程属性", hidden = true)
    @RequestMapping(value = "/doJobAttr", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doJobAttr(@RequestBody Map<String, Object> params) throws Exception{
        String method = "base/prodproc/doJobAttr";String methodName ="设置过程属性";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
        	Integer jobAttr=Integer.parseInt(params.get("jobAttr").toString());
            ApiResponseResult result = procProdService.doJobAttr(id, jobAttr);
            logger.debug("设置过程属性=doJobAttr:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置过程属性失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("设置过程属性失败！");
        }
    }
	
	@ApiOperation(value = "修改顺序", notes = "修改顺序", hidden = true)
    @RequestMapping(value = "/doProcOrder", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doProcOrder(@RequestBody Map<String, Object> params) throws Exception{
        String method = "base/prodproc/doProcOrder";String methodName ="修改顺序";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
        	Integer procOrder=Integer.parseInt(params.get("procOrder").toString());
            ApiResponseResult result = procProdService.doProcOrder(id, procOrder);
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
	
	
	
}
