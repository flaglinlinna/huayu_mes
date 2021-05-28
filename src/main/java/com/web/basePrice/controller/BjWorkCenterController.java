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
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.service.BjWorkCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("工作中心维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/workCenter")
public class BjWorkCenterController extends WebController{

    private String module = "工作中心维护信息";
    @Autowired
    private BjWorkCenterService workCenterService;

    @ApiOperation(value = "工作中心维护表结构", notes = "工作中心维护结构"+ BjWorkCenter.TABLE_NAME)
    @RequestMapping(value = "/getWorkCenter", method = RequestMethod.GET)
    @ResponseBody
    public BjWorkCenter getWorkCenter(){
        return new BjWorkCenter();
    }

    @ApiOperation(value = "工作中心维护列表页", notes = "工作中心维护列表页", hidden = true)
    @RequestMapping(value = "/toWorkCenter")
    public String toWorkCenter(){
        return "/web/basePrice/work_center";
    }

    @ApiOperation(value = "获取工作中心维护列表", notes = "获取工作中心维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/workCenter/getList";String methodName ="获取工作中心维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.ASC, "bsCode");
            ApiResponseResult result = workCenterService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取工作中心维护列表=getList:");
            //getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工作中心维护列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工作中心维护列表失败！");
        }
    }

    @ApiOperation(value = "新增工作中心维护", notes = "新增工作中心维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody BjWorkCenter workCenter) {
        String method = "basePrice/workCenter/add";String methodName ="新增工作中心维护";
        try{
            ApiResponseResult result = workCenterService.add(workCenter);
            logger.debug("新增工作中心维护=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("工作中心维护新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("工作中心维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑工作中心维护", notes = "编辑工作中心维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody BjWorkCenter workCenter){
        String method = "basePrice/workCenter/edit";String methodName ="编辑工作中心维护";
        try{
            ApiResponseResult result = workCenterService.edit(workCenter);
            logger.debug("编辑工作中心维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑工作中心维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑工作中心维护失败！");
        }
    }

    @ApiOperation(value = "根据ID获取工作中心维护详情", notes = "根据ID获取工作中心维护详情",hidden = true)
    @RequestMapping(value = "/getWorkCenterDetail", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getWorkCenter(@RequestBody Map<String, Object> params){
        String method = "basePrice/workCenter/getWorkCenterDetail";String methodName ="根据ID获取工作中心维护详情";
        long id = Long.parseLong(params.get("id").toString()) ;
        try{
            ApiResponseResult result = workCenterService.getWorkCenter(id);
            logger.debug("根据ID获取工作中心维护=getWorkCenter:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取工作中心维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工作中心维护失败！");
        }
    }

    @ApiOperation(value = "删除工作中心维护", notes = "删除工作中心维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/workCenter/delete";String methodName ="删除工作中心维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = workCenterService.delete(id);
            logger.debug("删除工作中心维护=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除工作中心维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除工作中心维护失败！");
        }
    }
    
    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/workCenter/doStatus";String methodName ="设置正常/禁用";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
            ApiResponseResult result = workCenterService.doStatus(id, bsStatus);
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
