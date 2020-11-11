package com.web.produce.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Defective;
import com.web.produce.entity.Scheduling;
import com.web.produce.entity.SchedulingItem;
import com.web.produce.entity.SchedulingProcess;
import com.web.produce.entity.SchedulingTemp;
import com.web.produce.service.SchedulingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Api(description = "排产信息管理模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/scheduling")
public class SchedulingController extends WebController {

    private String module = "排产信息";
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
        return "/web/produce/scheduling/scheduling";
    }

    @ApiOperation(value = "新增页", notes = "新增页", hidden = true)
    @RequestMapping(value = "/toSchedulingAdd")
    public String toschedulingAdd(){
        return "/web/produce/scheduling/scheduling_add";
    }

    @ApiOperation(value = "编辑页", notes = "编辑页", hidden = true)
    @RequestMapping(value = "/toSchedulingEdit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toSchedulingEdit(Long id){
        ModelAndView mav = new ModelAndView("/web/produce/scheduling/scheduling_edit");
        try{
            ApiResponseResult result = schedulingService.getSchedulData(id);
            mav.addObject("id", id);
            if(result != null){
                mav.addObject("mapData", result.getData());
            }else{
                mav.addObject("mapData", null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return mav;
    }

    @ApiOperation(value = "新增", notes = "新增", hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(Scheduling scheduling) {
        String method = "/produce/scheduling/add";String methodName ="新增";
        try{
            ApiResponseResult result = schedulingService.add(scheduling);
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
    public ApiResponseResult edit(Scheduling scheduling) {
        String method = "/produce/scheduling/edit";String methodName ="编辑";
        try{
            ApiResponseResult result = schedulingService.edit(scheduling);
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

    @ApiOperation(value = "删除", notes = "删除", hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(Long id) {
        String method = "/produce/scheduling/delete";String methodName ="删除";
        try{
            ApiResponseResult result = schedulingService.delete(id);
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
        String method = "/produce/scheduling/getList";String methodName ="获取排产信息列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = schedulingService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取排产信息列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取排产信息列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取排产信息列表失败！");
        }
    }

    @ApiOperation(value="导出模板", notes="导出模板", hidden = true)
    @RequestMapping(value = "/getExcel", method = RequestMethod.GET)
    @ResponseBody
    public void getExcel() {
        String method = "/produce/scheduling/getExcel";String methodName ="导出模板";
        try {
            schedulingService.getExcel(getResponse());
            logger.debug("导出模板=getExcel:");
            getSysLogService().success(module,method, methodName, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出模板失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
        }
    }

    @ApiOperation(value = "导入", notes = "导入", hidden = true)
    @RequestMapping(value = "/doExcel", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doExcel(MultipartFile file) throws Exception{
        String method = "/produce/scheduling/doExcel";String methodName ="导入";
        try{
            ApiResponseResult result = schedulingService.doExcel(file);
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

    @ApiOperation(value = "检验", notes = "检验", hidden = true)
    @RequestMapping(value = "/doCheckProc", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doCheckProc(){
        String method = "/produce/scheduling/doCheckProc";String methodName ="检验";
        try{
            ApiResponseResult result = schedulingService.doCheckProc();
            logger.debug("检验=doCheckProc:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("检验失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("检验失败！");
        }
    }

    @ApiOperation(value = "获取导入临时数据列表", notes = "获取导入临时数据列表", hidden = true)
    @RequestMapping(value = "/getTempList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getTempList(){
        String method = "/produce/scheduling/getTempList";String methodName ="获取导入临时数据列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = schedulingService.getTempList(super.getPageRequest(sort));
            logger.debug("获取导入临时数据列表=getTempList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取导入临时数据列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取导入临时数据列表失败！");
        }
    }

    @ApiOperation(value="根据当前登录用户删除临时表所有数据", notes="根据当前登录用户删除临时表所有数据", hidden = true)
    @ResponseBody
    @RequestMapping(value = "/deleteTempAll", method = RequestMethod.POST)
    public ApiResponseResult deleteTempAll(){
        String method = "/produce/scheduling/deleteTempAll";String methodName ="根据当前登录用户删除临时表所有数据";
        try {
            ApiResponseResult result = schedulingService.deleteTempAll();
            logger.debug("根据当前登录用户删除临时表所有数据=deleteTempAll:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据当前登录用户删除临时表所有数据失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("根据当前登录用户删除临时表所有数据失败！");
        }
    }

    @ApiOperation(value="确认临时数据", notes="确认临时数据", hidden = true)
    @ResponseBody
    @RequestMapping(value = "/confirmTemp", method = RequestMethod.POST)
    public ApiResponseResult confirmTemp(){
        String method = "/produce/scheduling/confirmTemp";String methodName ="确认临时数据";
        try {
            ApiResponseResult result = schedulingService.confirmTemp();
            logger.debug("确认临时数据=confirmTemp:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认临时数据失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("确认临时数据失败！");
        }
    }

    @ApiOperation(value="提取工序", notes="提取工序", hidden = true)
    @ResponseBody
    @RequestMapping(value = "/getProcessProc", method = RequestMethod.POST)
    public ApiResponseResult getProcessProc(){
        String method = "/produce/scheduling/getProcessProc";String methodName ="提取工序";
        try {
            ApiResponseResult result = schedulingService.getProcessProc();
            logger.debug("提取工序=getProcessProc:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取工序失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("提取工序失败！");
        }
    }

    @ApiOperation(value = "获取工艺列表", notes = "获取工艺列表", hidden = true)
    @RequestMapping(value = "/getProcessList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getProcessList(String keyword, Long mid){
        String method = "/produce/scheduling/getProcessList";String methodName ="获取工艺列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = schedulingService.getProcessList(keyword, mid, super.getPageRequest(sort));
            logger.debug("获取工艺列表=getProcessLst:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工艺列表失败！");
        }
    }

    @ApiOperation(value = "编辑工艺", notes = "编辑工艺", hidden = true)
    @RequestMapping(value = "/editProcess", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult editProcess(@RequestBody SchedulingProcess schedulingProcess){
        String method = "/produce/scheduling/editProcess";String methodName ="编辑工艺";
        try{
            ApiResponseResult result = schedulingService.editProcess(schedulingProcess);
            logger.debug("编辑工艺=editProcess:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("编辑工艺失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑工艺失败！");
        }
    }

    @ApiOperation(value = "保存工艺", notes = "保存工艺", hidden = true)
    @RequestMapping(value = "/saveProcessProc", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult saveProcessProc(Long mid, String processIds){
        String method = "/produce/scheduling/saveProcessProc";String methodName ="保存工艺";
        try{
            ApiResponseResult result = schedulingService.saveProcessProc(mid, processIds);
            logger.debug("保存工艺=saveProcess:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("保存工艺失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("保存工艺失败！");
        }
    }

    @ApiOperation(value = "获取组件列表", notes = "获取组件列表", hidden = true)
    @RequestMapping(value = "/getItemList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getItemList(String keyword, Long mid){
        String method = "/produce/scheduling/getItemList";String methodName ="获取组件列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = schedulingService.getItemList(keyword, mid, super.getPageRequest(sort));
            logger.debug("获取组件列表=getItemList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取组件列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取组件列表失败！");
        }
    }

    @ApiOperation(value = "获取上线人员列表", notes = "获取上线人员列表", hidden = true)
    @RequestMapping(value = "/getEmpList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getEmpList(Long mid){
        String method = "/produce/scheduling/getEmpList";String methodName ="获取上线人员列表";
        try {
//            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = schedulingService.getEmpList(mid, super.getPageRequest(Sort.unsorted()));
            logger.debug("获取上线人员列表=getEmpList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取上线人员列表！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取上线人员列表！");
        }
    }


    @ApiOperation(value = "获取生产投料列表", notes = "获取生产投料列表", hidden = true)
    @RequestMapping(value = "/getProdOrderList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getProdOrderList(Long mid){
        String method = "/produce/scheduling/getProdOrderList";String methodName ="获取生产投料列表";
        try {
//            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = schedulingService.getProdOrderList(mid, super.getPageRequest(Sort.unsorted()));
            logger.debug("获取生产投料列表=getProdOrderList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取生产投料列表！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取生产投料列表！");
        }
    }


    @ApiOperation(value = "编辑组件", notes = "编辑组件", hidden = true)
    @RequestMapping(value = "/editItem", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult editItem(SchedulingItem schedulingItem){
        String method = "/produce/scheduling/editItem";String methodName ="编辑组件";
        try {
            ApiResponseResult result = schedulingService.editItem(schedulingItem);
            logger.debug("编辑组件=editItem:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("编辑组件失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑组件失败！");
        }
    }
}
