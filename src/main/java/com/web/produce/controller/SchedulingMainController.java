package com.web.produce.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;


import com.web.produce.entity.SchedulingDet;
import com.web.produce.entity.SchedulingMain;
import com.web.produce.entity.SchedulingTemp;
import com.web.produce.service.SchedulingMainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(description = "排产信息管理模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/schedulingMain")
public class SchedulingMainController extends WebController {

    private String module = "排产信息 主";
    @Autowired
    private SchedulingMainService schedulingMainService;

    @ApiOperation(value = "排产信息主表结构", notes = "排产信息主表结构"+SchedulingMain.TABLE_NAME)
    @RequestMapping(value = "/getSchedulingMain", method = RequestMethod.GET)
    @ResponseBody
    public SchedulingMain getSchedulingMain(){
        return new SchedulingMain();
    }

    @ApiOperation(value = "排产信息从表结构", notes = "排产信息从表结构"+SchedulingDet.TABLE_NAME)
    @RequestMapping(value = "/getSchedulingDet", method = RequestMethod.GET)
    @ResponseBody
    public SchedulingDet getSchedulingDet(){
        return new SchedulingDet();
    }

    @ApiOperation(value = "排产信息列表页", notes = "排产信息列表页", hidden = true)
    @RequestMapping(value = "/toSchedulingMain")
    public String toSchedulingMain(){
        return "/web/produce/scheduling/scheduling_main";
    }

    @ApiOperation(value = "新增页", notes = "新增页", hidden = true)
    @RequestMapping(value = "/toSchedulingMainAdd")
    public String toSchedulingMainAdd(){
        return "/web/produce/scheduling/scheduling_main_add";
    }

    @ApiOperation(value = "新增", notes = "新增", hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody SchedulingMain schedulingMain) {
        String method = "/produce/schedulingMain/add";String methodName ="新增";
        try{
            ApiResponseResult result = schedulingMainService.add(schedulingMain);
            logger.debug("新增=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("新增失败！");
        }
    }

    @ApiOperation(value = "编辑", notes = "编辑", hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(SchedulingMain schedulingMain) {
        String method = "/produce/schedulingMain/edit";String methodName ="编辑";
        try{
            ApiResponseResult result = schedulingMainService.edit(schedulingMain);
            logger.debug("编辑=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑失败！");
        }
    }


    @ApiOperation(value = "部门下拉列表", notes = "部门下拉列表", hidden = true)
    @RequestMapping(value = "/getDeptSelect", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getDeptSelect() {
        String method = "/produce/schedulingMain/getDeptSelect";String methodName ="部门下拉列表";
        try{
            ApiResponseResult result = schedulingMainService.getDeptSelect();
            logger.debug("部门下拉列表=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("部门下拉列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("部门下拉列表失败！");
        }
    }

    @ApiOperation(value = "删除", notes = "删除", hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(Long id) {
        String method = "/produce/schedulingMain/delete";String methodName ="删除";
        try{
            ApiResponseResult result = schedulingMainService.delete(id);
            logger.debug("删除=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除失败！");
        }
    }

    @ApiOperation(value = "获取排产信息列表", notes = "获取排产信息列表", hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword){
        String method = "/produce/schedulingMain/getList";String methodName ="获取排产信息列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = schedulingMainService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取排产信息列表主=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取排产信息列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取排产信息列表失败！");
        }
    }

    @ApiOperation(value = "修改生效状态", notes = "修改生效状态", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(Long id, Integer status) {
        String method = "/produce/schedulingMain/doStatus";String methodName ="修改生效状态";
        try{
            ApiResponseResult result = schedulingMainService.doStatus(id, status);
            logger.debug("修改生效状态=doStatus:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("修改生效状态失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("修改生效状态失败！");
        }
    }
}