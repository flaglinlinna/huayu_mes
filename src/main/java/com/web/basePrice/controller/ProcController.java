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
import com.web.basePrice.entity.Proc;
import com.web.basePrice.service.ProcService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("工序信息维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/proc")
public class ProcController extends WebController{

    private String module = "工序信息维护";
    @Autowired
    private ProcService procService;

    @ApiOperation(value = "工序信息维护表结构", notes = "工序信息维护结构"+ Proc.TABLE_NAME)
    @RequestMapping(value = "/getProc", method = RequestMethod.GET)
    @ResponseBody
    public Proc getProc(){
        return new Proc();
    }

    @ApiOperation(value = "工序信息维护列表页", notes = "工序信息维护列表页", hidden = true)
    @RequestMapping(value = "/toProc")
    public String toProc(){
        return "/web/basePrice/proc";
    }

    @ApiOperation(value = "获取工序信息维护列表", notes = "获取工序信息维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/proc/getList";String methodName ="获取工序信息维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "workcenterId");
            ApiResponseResult result = procService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取工序信息维护列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工序信息维护列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工序信息维护列表失败！");
        }
    }

    @ApiOperation(value = "新增工序信息维护", notes = "新增工序信息维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody Proc proc) {
        String method = "basePrice/proc/add";String methodName ="新增工序信息维护";
        try{
            ApiResponseResult result = procService.add(proc);
            logger.debug("新增工序信息维护=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("工序信息维护新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("工序信息维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑工序信息维护", notes = "编辑工序信息维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody Proc proc){
        String method = "basePrice/proc/edit";String methodName ="编辑工序信息维护";
        try{
            ApiResponseResult result = procService.edit(proc);
            logger.debug("编辑工序信息维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑工序信息维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑工序信息维护失败！");
        }
    }

    @ApiOperation(value = "根据ID获取工序信息维护详情", notes = "根据ID获取工序信息维护详情",hidden = true)
    @RequestMapping(value = "/getProcDetail", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult getProc(@RequestBody Map<String, Object> params){
        String method = "basePrice/proc/getProcDetail";String methodName ="根据ID获取工序信息维护详情";
        long id = Long.parseLong(params.get("id").toString()) ;
        try{
            ApiResponseResult result = procService.getProc(id);
            logger.debug("根据ID获取工序信息维护=getProc:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取工序信息维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工序信息维护失败！");
        }
    }

    @ApiOperation(value = "删除工序信息维护", notes = "删除工序信息维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/proc/delete";String methodName ="删除工序信息维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = procService.delete(id);
            logger.debug("删除工序信息维护=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除工序信息维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除工序信息维护失败！");
        }
    }
    
    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/proc/doStatus";String methodName ="设置正常/禁用";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
            ApiResponseResult result = procService.doStatus(id, bsStatus);
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
    
    @ApiOperation(value = "获取工作中心信息", notes = "获取工作中心信息", hidden = true)
    @RequestMapping(value = "/getWorkCenterList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getWorkCenterList(String keyword) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/proc/getWorkCenterList";String methodName ="获取工作中心信息";
        try{
        	Sort sort = Sort.unsorted();
            ApiResponseResult result = procService.getWorkCenterList("01", keyword,super.getPageRequest(sort));
            logger.debug("获取工作中心信息=getWorkCenterList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取工作中心信息失败！", e);
            getSysLogService().error(module,method, methodName, keyword+";"+e.toString());
            return ApiResponseResult.failure("获取工作中心信息失败！");
        }
    }
}
