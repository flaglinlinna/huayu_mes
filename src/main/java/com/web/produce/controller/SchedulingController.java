package com.web.produce.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.SchedulingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Api(description = "排产信息管理模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "/scheduling")
public class SchedulingController extends WebController {

    @Autowired
    private SchedulingService schedulingService;

    @ApiOperation(value = "排产信息列表页", notes = "排产信息列表页", hidden = true)
    @RequestMapping(value = "/toScheduling")
    public String toscheduling(){
        return "/web/produce/scheduling";
    }

    @ApiOperation(value = "新增页", notes = "新增页", hidden = true)
    @RequestMapping(value = "/toSchedulingAdd")
    public String toschedulingAdd(){
        return "/web/produce/scheduling_add";
    }

    @ApiOperation(value = "获取排产信息列表", notes = "获取排产信息列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword){
        String method = "/scheduling/getList";String methodName ="获取排产信息列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = schedulingService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取排产信息列表=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取排产信息列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取排产信息列表失败！");
        }
    }

    @ApiOperation(value="导出模板", notes="导出模板")
    @RequestMapping(value = "/getExcel", method = RequestMethod.GET)
    @ResponseBody
    public void getExcel() {
        String method = "/scheduling/getExcel";String methodName ="导出模板";
        try {
            schedulingService.getExcel(getResponse());
            logger.debug("导出模板=getExcel:");
            getSysLogService().success(method, methodName, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出模板失败！", e);
            getSysLogService().error(method, methodName, e.toString());
        }
    }

    @ApiOperation(value = "导入", notes = "导入")
    @RequestMapping(value = "/doExcel", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doExcel(MultipartFile file) throws Exception{
        String method = "/scheduling/doExcel";String methodName ="导入";
        try{
            ApiResponseResult result = schedulingService.doExcel(file);
            logger.debug("导入=doExcel:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("导入失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("导入失败！");
        }
    }

    @ApiOperation(value="确认临时数据", notes="确认临时数据")
    @ResponseBody
    @RequestMapping(value = "/confirmTemp", method = RequestMethod.POST)
    public ApiResponseResult confirmTemp(){
        String method = "/scheduling/confirmTemp";String methodName ="导入";
        try {
            return schedulingService.confirmTemp();
        } catch (Exception e) {
            logger.error(e.toString(), e);
            e.printStackTrace();
            return ApiResponseResult.failure(e.getMessage());
        }
    }
}
