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
import com.web.basePrice.entity.PriceComm;
import com.web.basePrice.service.PriceCommService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Api("物料通用价格维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/priceComm")
public class PriceCommController extends WebController{

    private String module = "物料通用价格维护";
    @Autowired
    private PriceCommService priceCommService;

    @ApiOperation(value = "物料通用价格维护表结构", notes = "物料通用价格维护结构"+ PriceComm.TABLE_NAME)
    @RequestMapping(value = "/getPriceComm", method = RequestMethod.GET)
    @ResponseBody
    public PriceComm getPriceComm(){
        return new PriceComm();
    }

    @ApiOperation(value = "物料通用价格维护列表页", notes = "物料通用价格维护列表页", hidden = true)
    @RequestMapping(value = "/toPriceComm")
    public String toPriceComm(){
        return "/web/basePrice/price_comm";
    }

    @ApiOperation(value = "获取物料通用价格维护列表", notes = "获取物料通用价格维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/priceComm/getList";String methodName ="获取物料通用价格维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = priceCommService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取物料通用价格维护列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取物料通用价格维护列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取物料通用价格维护列表失败！");
        }
    }

    @ApiOperation(value = "新增物料通用价格维护", notes = "新增物料通用价格维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody PriceComm priceComm) {
        String method = "basePrice/priceComm/add";String methodName ="新增物料通用价格维护";
        try{
            ApiResponseResult result = priceCommService.add(priceComm);
            logger.debug("新增物料通用价格维护=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("物料通用价格维护新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("物料通用价格维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑物料通用价格维护", notes = "编辑物料通用价格维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody PriceComm priceComm){
        String method = "basePrice/priceComm/edit";String methodName ="编辑物料通用价格维护";
        try{
            ApiResponseResult result = priceCommService.edit(priceComm);
            logger.debug("编辑物料通用价格维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑物料通用价格维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑物料通用价格维护失败！");
        }
    }

    @ApiOperation(value = "删除物料通用价格维护", notes = "删除物料通用价格维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/priceComm/delete";String methodName ="删除物料通用价格维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = priceCommService.delete(id);
            logger.debug("删除物料通用价格维护=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除物料通用价格维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除物料通用价格维护失败！");
        }
    }
    
    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/priceComm/doStatus";String methodName ="设置正常/禁用";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
            ApiResponseResult result = priceCommService.doStatus(id, bsStatus);
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
    
    @ApiOperation(value = "获取单位下拉列表", notes = "获取单位下拉列表",hidden = true)
    @RequestMapping(value = "/getUnitList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getUnitList() {
        String method = "basePrice/priceComm/getUnitList";String methodName ="获取单位下拉列表";
        try {
            ApiResponseResult result = priceCommService.getUnitList();
            logger.debug("获取单位下拉列表=getUnitList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取单位下拉列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取单位下拉列表失败！");
        }
    }

    @ApiOperation(value = "获取物料下拉列表", notes = "获取物料下拉列表",hidden = true)
    @RequestMapping(value = "/getItemList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getItemList(String keyword) {
        String method = "basePrice/priceComm/getItemList";String methodName ="获取物料下拉列表";
        try {
            Sort sort = Sort.unsorted();
            ApiResponseResult result = priceCommService.getItemList(keyword,super.getPageRequest(sort));
            logger.debug("获取单位下拉列表=getUnitList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取物料下拉列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取物料下拉列表失败！");
        }
    }

    @ApiOperation(value = "导入", notes = "导入", hidden = true)
    @RequestMapping(value = "/doExcel", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doExcel(MultipartFile[] file) throws Exception{
        String method = "/basePrice/priceComm/doExcel";String methodName ="导入";
        try{
            ApiResponseResult result = priceCommService.doExcel(file);
            logger.debug("导入=doExcel:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("导入失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("导入失败！");
        }
    }

    @ApiOperation(value = "导出", notes = "导出", hidden = true)
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void export(HttpServletResponse response, String keyword) throws Exception{
        String method = "/basePrice/priceComm/export";String methodName ="导出";
        try{
            priceCommService.exportExcel(response,keyword);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("导出失败！", e);
        }
    }
}
