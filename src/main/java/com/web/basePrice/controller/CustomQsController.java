package com.web.basePrice.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.CustomQs;
import com.web.basePrice.service.CustomQsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api("客户品质标准模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/customQs")
public class CustomQsController extends WebController{

    private String module = "客户品质标准";
    @Autowired
    private CustomQsService customQsService;

    @ApiOperation(value = "客户品质标准表结构", notes = "客户品质标准表结构"+ CustomQs.TABLE_NAME)
    @RequestMapping(value = "/getProfitProd", method = RequestMethod.GET)
    @ResponseBody
    public CustomQs getCustomQs(){
        return new CustomQs();
    }

    @ApiOperation(value = "客户品质标准列表页", notes = "客户品质标准列表页", hidden = true)
    @RequestMapping(value = "/toCustomQs")
    public String toCustomQs(){
        return "/web/basePrice/custom_qs";
    }

    @ApiOperation(value = "获取客户品质标准列表", notes = "获取客户品质标准列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/customQs/getList";String methodName ="获取客户品质标准列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = customQsService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取客户品质标准列表=getList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取客户品质标准列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取客户品质标准列表失败！");
        }
    }


    @ApiOperation(value = "获取客户品质标准类别", notes = "获取客户品质标准类别",hidden = true)
    @RequestMapping(value = "/getQsType", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getQsType(String keyword) {
        String method = "basePrice/customQs/getQsType";String methodName ="获取客户品质标准类别";
        try {
            Sort sort = Sort.unsorted();
            ApiResponseResult result = customQsService.getQsType(keyword,super.getPageRequest(sort));
            logger.debug("获取客户品质标准类别=getQsType:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取客户品质标准类别失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取客户品质标准类别失败！");
        }
    }


    @ApiOperation(value = "新增客户品质标准信息", notes = "新增客户品质标准信息",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody CustomQs customQs) {
        String method = "basePrice/customQs/add";String methodName ="新增客户品质标准信息";
        try{
            ApiResponseResult result = customQsService.add(customQs);
            logger.debug("新增客户品质标准信息=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增客户品质标准新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("新增客户品质标准新增失败！");
        }
    }

    @ApiOperation(value = "编辑客户品质标准信息", notes = "编辑客户品质标准信息",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody CustomQs customQs){
        String method = "basePrice/customQs/edit";String methodName ="编辑客户品质标准信息";
        try{
            ApiResponseResult result = customQsService.edit(customQs);
            logger.debug("编辑客户品质标准信息=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑客户品质标准失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑客户品质标准失败！");
        }
    }

    @ApiOperation(value = "删除客户品质标准信息", notes = "删除客户品质标准信息",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/customQs/delete";String methodName ="删除客户品质标准信息";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = customQsService.delete(id);
            logger.debug("删除客户品质标准信息=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除客户品质标准信息失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除客户品质标准信息失败！");
        }
    }

    @ApiOperation(value = "删除客户品质标准附件", notes = "删除客户品质标准信息",hidden = true)
    @RequestMapping(value = "/delFile", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delFile(Long recordId, Long fileId){
        String method = "basePrice/customQs/delFile";String methodName ="删除客户品质标准附件";
        try{
            ApiResponseResult result = customQsService.delFile(recordId);
            logger.debug("删除客户品质标准附件=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除客户品质标准附件失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除客户品质标准附件失败！");
        }
    }

    

}
