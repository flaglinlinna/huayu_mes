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
import com.web.basePrice.entity.ProdTyp;
import com.web.basePrice.service.ProdTypService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("产品类型维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/prodTyp")
public class ProdTypController extends WebController{

    private String module = "产品类型维护";
    @Autowired
    private ProdTypService prodTypService;

    @ApiOperation(value = "产品类型维护表结构", notes = "产品类型维护结构"+ ProdTyp.TABLE_NAME)
    @RequestMapping(value = "/getProdTyp", method = RequestMethod.GET)
    @ResponseBody
    public ProdTyp getProdTyp(){
        return new ProdTyp();
    }

    @ApiOperation(value = "产品类型维护列表页", notes = "产品类型维护列表页", hidden = true)
    @RequestMapping(value = "/toProdTyp")
    public String toProdTyp(){
        return "/web/basePrice/prodTyp";
    }

    @ApiOperation(value = "获取产品类型维护列表", notes = "获取产品类型维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/prodTyp/getList";String methodName ="获取产品类型维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = prodTypService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取产品类型维护列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取产品类型维护列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取产品类型维护列表失败！");
        }
    }

    @ApiOperation(value = "新增产品类型维护", notes = "新增产品类型维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody ProdTyp prodTyp) {
        String method = "basePrice/prodTyp/add";String methodName ="新增产品类型维护";
        try{
            ApiResponseResult result = prodTypService.add(prodTyp);
            logger.debug("新增产品类型维护=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("产品类型维护新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("产品类型维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑产品类型维护", notes = "编辑产品类型维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody ProdTyp prodTyp){
        String method = "basePrice/prodTyp/edit";String methodName ="编辑产品类型维护";
        try{
            ApiResponseResult result = prodTypService.edit(prodTyp);
            logger.debug("编辑产品类型维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑产品类型维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑产品类型维护失败！");
        }
    }

    @ApiOperation(value = "删除产品类型维护", notes = "删除产品类型维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/prodTyp/delete";String methodName ="删除产品类型维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = prodTypService.delete(id);
            logger.debug("删除产品类型维护=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除产品类型维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除产品类型维护失败！");
        }
    }
    
    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/prodTyp/doStatus";String methodName ="设置正常/禁用";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
            ApiResponseResult result = prodTypService.doStatus(id, bsStatus);
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
}
