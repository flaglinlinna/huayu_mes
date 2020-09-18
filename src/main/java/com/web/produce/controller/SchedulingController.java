package com.web.produce.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Defective;
import com.web.produce.entity.Scheduling;
import com.web.produce.entity.SchedulingTemp;
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
@RequestMapping(value = "produce/scheduling")
public class SchedulingController extends WebController {

    @Autowired
    private SchedulingService schedulingService;

    @ApiOperation(value = "排产信息表结构", notes = "排产信息表结构"+Scheduling.TABLE_NAME)
    @RequestMapping(value = "/getScheduling", method = RequestMethod.GET)
    @ResponseBody
    public Scheduling getScheduling(){
        return new Scheduling();
    }

    @ApiOperation(value = "排产信息导入临时表结构", notes = "排产信息导入临时表结构"+SchedulingTemp.TABLE_NAME)
    @RequestMapping(value = "/getSchedulingTemp", method = RequestMethod.GET)
    @ResponseBody
    public SchedulingTemp getSchedulingTemp(){
        return new SchedulingTemp();
    }


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

    @ApiOperation(value = "获取排产信息列表", notes = "获取排产信息列表", hidden = true)
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

    @ApiOperation(value="导出模板", notes="导出模板", hidden = true)
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

    @ApiOperation(value = "导入", notes = "导入", hidden = true)
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


    @ApiOperation(value = "获取导入临时数据列表", notes = "获取导入临时数据列表", hidden = true)
    @RequestMapping(value = "/getTempList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getTempList(){
        String method = "/scheduling/getTempList";String methodName ="获取导入临时数据列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = schedulingService.getTempList(super.getPageRequest(sort));
            logger.debug("获取导入临时数据列表=getTempList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取导入临时数据列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取导入临时数据列表失败！");
        }
    }

    @ApiOperation(value="根据当前登录用户删除临时表所有数据", notes="根据当前登录用户删除临时表所有数据", hidden = true)
    @ResponseBody
    @RequestMapping(value = "/deleteTempAll", method = RequestMethod.POST)
    public ApiResponseResult deleteTempAll(){
        String method = "/scheduling/deleteTempAll";String methodName ="根据当前登录用户删除临时表所有数据";
        try {
            ApiResponseResult result = schedulingService.deleteTempAll();
            logger.debug("根据当前登录用户删除临时表所有数据=deleteTempAll:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据当前登录用户删除临时表所有数据失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("根据当前登录用户删除临时表所有数据失败！");
        }
    }

    @ApiOperation(value="确认临时数据", notes="确认临时数据", hidden = true)
    @ResponseBody
    @RequestMapping(value = "/confirmTemp", method = RequestMethod.POST)
    public ApiResponseResult confirmTemp(){
        String method = "/scheduling/confirmTemp";String methodName ="确认临时数据";
        try {
            ApiResponseResult result = schedulingService.confirmTemp();
            logger.debug("确认临时数据=confirmTemp:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认临时数据失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("确认临时数据失败！");
        }
    }
}
