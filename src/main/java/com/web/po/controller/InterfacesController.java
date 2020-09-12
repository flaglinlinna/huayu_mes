package com.web.po.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.po.entity.Interfaces;
import com.web.po.entity.InterfacesRequest;
import com.web.po.service.InterfacesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Api(description = "接口信息配置管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/interfaces")
public class InterfacesController extends WebController {

    @Autowired
    private InterfacesService interfacesService;

    @ApiOperation(value = "新增接口配置", notes = "新增接口配置")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(Interfaces interfaces){
        String method = "/interfaces/add";String methodName ="新增接口配置";
        try{
            ApiResponseResult result = interfacesService.add(interfaces);
            logger.debug("新增接口配置=add:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("新增接口配置失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("新增接口配置失败！");
        }
    }

    @ApiOperation(value = "编辑接口配置", notes = "编辑接口配置")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(Interfaces interfaces){
        String method = "/interfaces/edit";String methodName ="编辑接口配置";
        try{
            ApiResponseResult result = interfacesService.edit(interfaces);
            logger.debug("编辑接口配置=edit:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("编辑接口配置失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("编辑接口配置失败！");
        }
    }

    @ApiOperation(value = "删除接口配置", notes = "删除接口配置")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestParam(value = "id", required = false) Long id){
        String method = "/interfaces/delete";String methodName ="删除接口配置";
        try{
            ApiResponseResult result = interfacesService.delete(id);
            logger.debug("删除接口配置=delete:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除接口配置失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("删除接口配置失败！");
        }
    }

    @ApiOperation(value = "接口配置列表页", notes = "接口配置列表页", hidden = true)
    @RequestMapping(value = "/toInterfacesList")
    public String toInterfacesList(){
        return "/api/interfaces/io_list";
    }

    @ApiOperation(value = "获取接口配置列表", notes = "获取接口配置列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword, String bsCode, String bsName, Integer bsStatus, Date createdTimeStart, Date createdTimeEnd){
        String method = "/interfaces/getList";String methodName ="获取接口配置列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = interfacesService.getList(keyword, bsCode, bsName, bsStatus, createdTimeStart, createdTimeEnd, super.getPageRequest(sort));
            logger.debug("获取接口配置列表=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取接口配置列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取接口配置列表失败！");
        }
    }

    @ApiOperation(value = "根据ID获取接口配置", notes = "根据ID获取接口配置")
    @RequestMapping(value = "/getInterfaces", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getInterfaces(Long id){
        String method = "/interfaces/getInterfaces";String methodName ="根据ID获取接口配置";
        try{
            ApiResponseResult result = interfacesService.getInterfaces(id);
            logger.debug("根据ID获取接口配置=getInterfaces:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取接口配置失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("根据ID获取接口配置失败！");
        }
    }

    @ApiOperation(value = "设置启用/禁用", notes = "设置启用/禁用")
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        String method = "/interfaces/doStatus";String methodName ="设置启用/禁用";
        try{
            ApiResponseResult result = interfacesService.doStatus(id, bsStatus);
            logger.debug("设置启用/禁用=doStatus:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置启用/禁用失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("设置启用/禁用失败！");
        }
    }


    @ApiOperation(value = "接口配置列表页", notes = "接口配置列表页", hidden = true)
    @RequestMapping(value = "/toConfigure")
    public ModelAndView toConfigure(Long id){
        ModelAndView mav = new ModelAndView("/api/interfaces/io_configure");
        mav.addObject("interId", id);
        try{
            //获取接口信息
            ApiResponseResult result = interfacesService.getInterfaces(id);
            if(result != null){
                mav.addObject("interfaces", result.getData());
            }
        }catch (Exception e){
        }
        return mav;
    }

    @ApiOperation(value = "新增请求参数", notes = "新增请求参数")
    @RequestMapping(value = "/addRequest", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult addRequest(InterfacesRequest request){
        String method = "/interfaces/addRequest";String methodName ="新增请求参数";
        try{
            ApiResponseResult result = interfacesService.addRequest(request);
            logger.debug("新增请求参数=addRequest:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("新增请求参数失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("新增请求参数失败！");
        }
    }

    @ApiOperation(value = "编辑请求参数", notes = "编辑请求参数")
    @RequestMapping(value = "/editRequest", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult editRequest(InterfacesRequest request){
        String method = "/interfaces/editRequest";String methodName ="编辑请求参数";
        try{
            ApiResponseResult result = interfacesService.editRequest(request);
            logger.debug("编辑请求参数=editRequest:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("编辑请求参数失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("编辑请求参数失败！");
        }
    }

    @ApiOperation(value = "删除请求参数", notes = "删除请求参数")
    @RequestMapping(value = "/deleteRequest", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult deleteRequest(@RequestParam(value = "id", required = false) Long id){
        String method = "/interfaces/deleteRequest";String methodName ="删除请求参数";
        try{
            ApiResponseResult result = interfacesService.deleteRequest(id);
            logger.debug("删除请求参数=deleteRequest:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除请求参数失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("删除请求参数失败！");
        }
    }

    @ApiOperation(value = "获取请求参数列表", notes = "获取请求参数列表")
    @RequestMapping(value = "/getRequestList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getRequestList(String keyword, Long interId){
        String method = "/interfaces/getRequestList";String methodName ="获取请求参数列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = interfacesService.getRequestList(keyword, interId, super.getPageRequest(sort));
            logger.debug("获取请求参数列表=getRequestList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取请求参数列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取请求参数列表失败！");
        }
    }

    @ApiOperation(value = "根据ID获取请求参数", notes = "根据ID获取请求参数")
    @RequestMapping(value = "/getRequest", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getRequest(Long id){
        String method = "/interfaces/getRequest";String methodName ="根据ID获取请求参数";
        try{
            ApiResponseResult result = interfacesService.getRequest(id);
            logger.debug("根据ID获取请求参数=getRequest:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("根据ID获取请求参数失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("根据ID获取请求参数失败！");
        }
    }

    @ApiOperation(value = "批量删除请求参数", notes = "批量删除请求参数")
    @RequestMapping(value = "/deleteRequestAll", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult deleteRequestAll(String idsStr){
        String method = "/interfaces/deleteRequestAll";String methodName ="批量删除请求参数";
        try{
            ApiResponseResult result = interfacesService.deleteRequestAll(idsStr);
            logger.debug("批量删除请求参数=deleteRequestAll:");
            getSysLogService().success(method, methodName, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("批量删除请求参数失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("批量删除请求参数失败！");
        }
    }
}
