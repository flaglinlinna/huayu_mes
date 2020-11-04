package com.web.baseInfo.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.baseInfo.entity.Unit;
import com.web.baseInfo.service.UnitService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("基本单位维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "baseInfo/unit")
public class UnitController extends WebController{

    @Autowired
    private UnitService unitService;

    @ApiOperation(value = "基本单位维护表结构", notes = "基本单位维护结构"+Unit.TABLE_NAME)
    @RequestMapping(value = "/getUnit", method = RequestMethod.GET)
    @ResponseBody
    public Unit getUnit(){
        return new Unit();
    }

    @ApiOperation(value = "基本单位维护列表页", notes = "基本单位维护列表页", hidden = true)
    @RequestMapping(value = "/toUnit")
    public String toUnit(){
        return "/web/baseInfo/unit";
    }

    @ApiOperation(value = "获取基本单位维护列表", notes = "获取基本单位维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "base/unit/getList";String methodName ="获取基本单位维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = unitService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取基本单位维护列表=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取基本单位维护列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取基本单位维护列表失败！");
        }
    }

    @ApiOperation(value = "新增基本单位维护", notes = "新增基本单位维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody Unit unit) {
        String method = "base/unit/add";String methodName ="新增基本单位维护";
        try{
            ApiResponseResult result = unitService.add(unit);
            logger.debug("新增基本单位维护=add:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("基本单位维护新增失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("基本单位维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑基本单位维护", notes = "编辑基本单位维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody Unit unit){
        String method = "base/unit/edit";String methodName ="编辑基本单位维护";
        try{
            ApiResponseResult result = unitService.edit(unit);
            logger.debug("编辑基本单位维护=edit:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑基本单位维护失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("编辑基本单位维护失败！");
        }
    }

    @ApiOperation(value = "根据ID获取基本单位维护详情", notes = "根据ID获取基本单位维护详情",hidden = true)
    @RequestMapping(value = "/getUnitDetail", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getUnit(@RequestBody Map<String, Object> params){
        String method = "base/unit/getUnitDetail";String methodName ="根据ID获取基本单位维护详情";
        long id = Long.parseLong(params.get("id").toString()) ;
        try{
            ApiResponseResult result = unitService.getUnit(id);
            logger.debug("根据ID获取基本单位维护=getUnit:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取基本单位维护失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取基本单位维护失败！");
        }
    }

    @ApiOperation(value = "删除基本单位维护", notes = "删除基本单位维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "base/unit/delete";String methodName ="删除基本单位维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = unitService.delete(id);
            logger.debug("删除基本单位维护=delete:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除基本单位维护失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("删除基本单位维护失败！");
        }
    }

}
