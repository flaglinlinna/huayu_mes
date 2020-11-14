package com.web.produce.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.SchedulingTemp;
import com.web.produce.service.SchedulingTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "排产导入临时信息管理模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/scheduling_temp")
public class SchedulingTempController extends WebController {

    private String module = "排产导入";
    @Autowired
    private SchedulingTempService schedulingTempService;

    @ApiOperation(value = "排产信息导入临时表结构", notes = "排产信息导入临时表结构"+SchedulingTemp.TABLE_NAME)
    @RequestMapping(value = "/getSchedulingTemp", method = RequestMethod.GET)
    @ResponseBody
    public SchedulingTemp getSchedulingTemp(){
        return new SchedulingTemp();
    }

    @ApiOperation(value = "排产导入临时信息列表页", notes = "排产导入临时信息列表页", hidden = true)
    @RequestMapping(value = "/toTemp")
    public String toTemp(){
        return "/web/produce/scheduling_temp/temp";
    }

    @ApiOperation(value = "删除排产导入临时信息", notes = "删除排产导入临时信息", hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(String ids){
        String method = "/produce/scheduling_temp/delete";String methodName ="删除排产导入临时信息";
        try{
            ApiResponseResult result = schedulingTempService.delete(ids);
            logger.debug(methodName+"=delete:");
            getSysLogService().success(module,method, methodName, ids);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName,ids+ e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
    }

    @ApiOperation(value = "获取排产导入临时信息列表", notes = "获取排产导入临时信息列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getHistoryList(String keyword, String startTime, String endTime){
        String method = "/produce/scheduling_temp/getList";String methodName ="获取排产导入临时信息列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            ApiResponseResult result = schedulingTempService.getList(keyword, startTime, endTime, super.getPageRequest(sort));
            logger.debug(methodName+"=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
    }

    @ApiOperation(value = "排产导入临时导入", notes = "排产导入临时导入", hidden = true)
    @RequestMapping(value = "/doExcel", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doExcel(MultipartFile file) throws Exception{
        String method = "/produce/scheduling_temp/doExcel";String methodName ="排产导入临时导入";
        try{
            ApiResponseResult result = schedulingTempService.doExcel(file);
            logger.debug(methodName+"=doExcel:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
    }

    @ApiOperation(value = "排产导入临时检验", notes = "排产导入临时检验", hidden = true)
    @RequestMapping(value = "/doCheckProc", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doCheckProc(String ids){
        String method = "/produce/scheduling_temp/doCheckProc";String methodName ="排产导入临时检验";
        try{
            ApiResponseResult result = schedulingTempService.doCheckProc(ids);
            logger.debug(methodName+"=doCheckProc:");
            getSysLogService().success(module,method, methodName, ids);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName, ids+e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
    }

    @ApiOperation(value = "排产导入临时生效", notes = "排产导入临时生效", hidden = true)
    @RequestMapping(value = "/doEffect", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doEffect(String ids){
        String method = "/produce/scheduling_temp/doEffect";String methodName ="排产导入临时生效";
        try{
            ApiResponseResult result = schedulingTempService.doEffect(ids);
            logger.debug(methodName+"=doEffect:");
            getSysLogService().success(module,method, methodName, ids);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName, ids+e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
    }
}
