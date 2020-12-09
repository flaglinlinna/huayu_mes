package com.web.basePrice.controller;

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

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.MjProcPrice;
import com.web.basePrice.service.MjProcPriceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("模具加工费率维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/mjProcPrice")
public class MjProcPriceController extends WebController{

    private String module = "模具加工费率维护";
    @Autowired
    private MjProcPriceService mjProcPriceService;

    @ApiOperation(value = "模具加工费率维护表结构", notes = "模具加工费率维护结构"+ MjProcPrice.TABLE_NAME)
    @RequestMapping(value = "/getMjProcPrice", method = RequestMethod.GET)
    @ResponseBody
    public MjProcPrice getMjProcPrice(){
        return new MjProcPrice();
    }

    @ApiOperation(value = "模具加工费率维护列表页", notes = "模具加工费率维护列表页", hidden = true)
    @RequestMapping(value = "/toMjProcPrice")
    public String toMjProcPrice(){
        return "/web/basePrice/mjproc_price";
    }

    @ApiOperation(value = "获取模具加工费率维护列表", notes = "获取模具加工费率维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/mjProcPrice/getList";String methodName ="获取模具加工费率维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = mjProcPriceService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取模具加工费率维护列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取模具加工费率维护列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取模具加工费率维护列表失败！");
        }
    }

    @ApiOperation(value = "新增模具加工费率维护", notes = "新增模具加工费率维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody MjProcPrice mjProcPrice) {
        String method = "basePrice/mjProcPrice/add";String methodName ="新增模具加工费率维护";
        try{
            ApiResponseResult result = mjProcPriceService.add(mjProcPrice);
            logger.debug("新增模具加工费率维护=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("模具加工费率维护新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("模具加工费率维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑模具加工费率维护", notes = "编辑模具加工费率维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody MjProcPrice mjProcPrice){
        String method = "basePrice/mjProcPrice/edit";String methodName ="编辑模具加工费率维护";
        try{
            ApiResponseResult result = mjProcPriceService.edit(mjProcPrice);
            logger.debug("编辑模具加工费率维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑模具加工费率维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑模具加工费率维护失败！");
        }
    }

    @ApiOperation(value = "删除模具加工费率维护", notes = "删除模具加工费率维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/mjProcPrice/delete";String methodName ="删除模具加工费率维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = mjProcPriceService.delete(id);
            logger.debug("删除模具加工费率维护=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除模具加工费率维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除模具加工费率维护失败！");
        }
    }
    
    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/mjProcPrice/doStatus";String methodName ="设置正常/禁用";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
            ApiResponseResult result = mjProcPriceService.doStatus(id, bsStatus);
            logger.debug("设置正常/禁用=doStatus:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置正常/禁用失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("设置正常/禁用失败！");
        }
    }
    
    @ApiOperation(value = "获取工序信息", notes = "获取工序信息", hidden = true)
    @RequestMapping(value = "/getProcList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getProcList(String keyword) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/mjProcPrice/getProcList";String methodName ="获取工序信息";
        try{
        	Sort sort = Sort.unsorted();
            ApiResponseResult result = mjProcPriceService.getProcList("01", keyword,super.getPageRequest(sort));
            logger.debug("获取工序信息=getProcList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取工序信息失败！", e);
            getSysLogService().error(module,method, methodName, keyword+";"+e.toString());
            return ApiResponseResult.failure("获取工序信息失败！");
        }
    }
}
