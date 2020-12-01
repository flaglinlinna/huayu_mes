package com.web.produce.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.BadMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(description = "来料不良录入")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/badMaterial")
public class BadMaterialController extends WebController {
    private String module = "来料不良录入";
    @Autowired
    private BadMaterialService badMaterialService;

    @ApiOperation(value = "来料不良录入页", notes = "来料不良录入页", hidden = true)
    @RequestMapping(value = "/toBadMaterial")
    public String toInputCheck(){
        return "/web/produce/bad_material/bad_material";
    }

    @ApiOperation(value="获取物料编码信息", notes="获取物料编码信息", hidden = true)
    @RequestMapping(value = "/getOrg", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getOrg(String keyword) {
        String method = "/badMaterial/getOrg";String methodName ="获取物料编码信息";
        try {
            Sort sort = Sort.unsorted();
            ApiResponseResult result = badMaterialService.getOrg(keyword,super.getPageRequest(sort));
            logger.debug("获取物料编码信息=getTaskNo:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取物料编码信息失败！", e);
            getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
            return ApiResponseResult.failure("获取物料编码信息失败！");
        }
    }

    @ApiOperation(value="获取部门下拉信息", notes="获取部门下拉信息", hidden = true)
    @RequestMapping(value = "/getDept", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getDept() {
        String method = "/badMaterial/getDept";String methodName ="获取部门下拉信息";
        try {
            ApiResponseResult result = badMaterialService.getDept();
            logger.debug("获取部门下拉信息=getTaskNo:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取部门下拉信息失败！", e);
            getSysLogService().error(module,method, methodName,e.toString());
            return ApiResponseResult.failure("获取部门下拉信息失败！");
        }
    }

    @ApiOperation(value="供应商查询框", notes="供应商查询框", hidden = true)
    @RequestMapping(value = "/getSupplier", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getSupplier(String keyword) {
        String method = "/badMaterial/getTaskNo";String methodName ="供应商查询框";
        try {
            Sort sort = Sort.unsorted();
            ApiResponseResult result = badMaterialService.getSupplier(keyword,super.getPageRequest(sort));
            logger.debug("供应商查询框=getTaskNo:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取供应商查询框信息失败！", e);
            getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
            return ApiResponseResult.failure("获取供应商查询框信息失败！");
        }
    }

    @ApiOperation(value="不良内容下拉信息", notes="不良内容下拉信息", hidden = true)
    @RequestMapping(value = "/getBadList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getBadList(String keyword) {
        String method = "/badMaterial/getBadList";String methodName ="获取不良内容下拉信息";
        try {
            ApiResponseResult result = badMaterialService.getBadList("","",keyword);
            logger.debug("获取不良内容下拉信息=getBadList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取不良内容下拉信息失败！", e);
            getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
            return ApiResponseResult.failure("获取不良内容下拉信息失败！");
        }
    }



    @ApiOperation(value="确认投入", notes="确认投入", hidden = true)
    @RequestMapping(value = "/saveMaterial", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult  saveMaterial(String itemNo,Integer deptId, Integer venderId,String prodDate,
                                           String lotNo,String defectCode,String defectQty){
        String method = "/badMaterial/addPut";String methodName ="确认投入";
//        String param = "条码:"+ barcode +";生产指令号"+task_no+";物料编码:"+item_no+";数量"+qty;
        try {
            ApiResponseResult result = badMaterialService.saveMaterial(itemNo,deptId,venderId,prodDate,lotNo,defectCode,defectQty);
            logger.debug("确认投入=addPut:");
            getSysLogService().success(module,method, methodName, "");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认投入失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("确认投入失败！");
        }
    }



    @ApiOperation(value="根据条码获取物料信息", notes="根据条码获取物料信息", hidden = true)
    @RequestMapping(value = "/getDetailByBarcode", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getDetailByTask(String barcode) {
        String method = "/badMaterial/getDetailByBarcode";String methodName ="根据条码获取物料信息";
        String params = "条码:"+barcode;
        try {
            ApiResponseResult result = badMaterialService.getInfoBarcode(barcode);
            logger.debug("根据条码获取物料信息=getDetailByBarcode:");
//            getSysLogService().success(module,method, methodName, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据条码获取物料信息失败！", e);
            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
            return ApiResponseResult.failure("根据条码获取物料信息失败！");
        }
    }

    @ApiOperation(value = "获取历史列表", notes = "获取历史列表")
    @RequestMapping(value = "/getHistoryList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getHistoryList(
            @RequestParam(value = "hkeywork", required = false) String hkeywork,
            @RequestParam(value = "hStartTime", required = false) String hStartTime,
            @RequestParam(value = "hEndTime", required = false) String hEndTime){
        String method = "/badMaterial/getHistoryList";String methodName ="获取历史列表";
        String param = "关键词:"+hkeywork+";开始时间"+ hStartTime +"结束时间:"+hEndTime;
        try {
            Sort sort =  Sort.unsorted();
            ApiResponseResult result =badMaterialService.getHistoryList(hkeywork,hStartTime,hEndTime, super.getPageRequest(sort));
            logger.debug(methodName+"=getList:");
//            getSysLogService().success(module,method, methodName, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName,param+";"+ e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
    }
}
