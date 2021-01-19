package com.web.basePrice.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.BjModelType;
import com.web.basePrice.service.BjModelTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api("机台类型维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/modelType")
public class BjModelTypeController extends WebController{

    private String module = "机台类型维护";
    @Autowired
    private BjModelTypeService bjModelTypeService;

    @ApiOperation(value = "机台类型维护表结构", notes = "机台类型维护结构"+ BjModelType.TABLE_NAME)
    @RequestMapping(value = "/getBjModelType", method = RequestMethod.GET)
    @ResponseBody
    public BjModelType getBjModelType(){
        return new BjModelType();
    }

    @ApiOperation(value = "机台类型维护列表页", notes = "机台类型维护列表页", hidden = true)
    @RequestMapping(value = "/toBjModelType")
    public String toBjModelType(){
        return "/web/basePrice/model_type";
    }

    @ApiOperation(value = "获取机台类型维护列表", notes = "获取机台类型维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/bjModelType/getList";String methodName ="获取机台类型维护列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = bjModelTypeService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取机台类型维护列表=getList:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取机台类型维护列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取机台类型维护列表失败！");
        }
    }

    @ApiOperation(value = "新增机台类型维护", notes = "新增机台类型维护",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody BjModelType bjModelType) {
        String method = "basePrice/bjModelType/add";String methodName ="新增机台类型维护";
        try{
            ApiResponseResult result = bjModelTypeService.add(bjModelType);
            logger.debug("新增机台类型维护=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("机台类型维护新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("机台类型维护新增失败！");
        }
    }

    @ApiOperation(value = "编辑机台类型维护", notes = "编辑机台类型维护",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody BjModelType bjModelType){
        String method = "basePrice/bjModelType/edit";String methodName ="编辑机台类型维护";
        try{
            ApiResponseResult result = bjModelTypeService.edit(bjModelType);
            logger.debug("编辑机台类型维护=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑机台类型维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑机台类型维护失败！");
        }
    }

    @ApiOperation(value = "删除机台类型维护", notes = "删除机台类型维护",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/bjModelType/delete";String methodName ="删除机台类型维护";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = bjModelTypeService.delete(id);
            logger.debug("删除机台类型维护=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除机台类型维护失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除机台类型维护失败！");
        }
    }

    
    @ApiOperation(value = "获取工序信息", notes = "获取工序信息", hidden = true)
    @RequestMapping(value = "/getProcList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getProcList(String keyword) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/bjModelType/getProcList";String methodName ="获取工序信息";
        try{
        	Sort sort = Sort.unsorted();
        	ApiResponseResult result = bjModelTypeService.getProcList("01", keyword,super.getPageRequest(sort));
            logger.debug("获取工序信息=getProcList:");
            getSysLogService().success(module,method, methodName, keyword);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取工序信息失败！", e);
            getSysLogService().error(module,method, methodName, keyword+";"+e.toString());
            return ApiResponseResult.failure("获取工序信息失败！");
        }
    }
    
    @ApiOperation(value = "获取机台类型信息", notes = "获取机台类型信息", hidden = true)
    @RequestMapping(value = "/getType", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getType() throws Exception{
        String method = "basePrice/bjModelType/getType";String methodName ="获取机台类型信息";
        try{
        	Sort sort = Sort.unsorted();
            ApiResponseResult result = bjModelTypeService.getType("BJ_BASE_MACHINE_TYPE",super.getPageRequest(sort));
            logger.debug("获取机台类型信息=getType:");
            getSysLogService().success(module,method, methodName,"BJ_BASE_MACHINE_TYPE");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取机台类型信息失败！", e);
            getSysLogService().error(module,method, methodName, "BJ_BASE_MACHINE_TYPE;"+e.toString());
            return ApiResponseResult.failure("获取机台类型信息失败！");
        }
    }
    
    @ApiOperation(value = "获取工作中心信息", notes = "获取工作中心信息", hidden = true)
    @RequestMapping(value = "/getWorkCenterList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getWorkCenterList(String keyword) throws Exception{
        String method = "basePrice/bjModelType/getWorkCenterList";String methodName ="获取工作中心信息";
        try{
        	Sort sort = Sort.unsorted();
            ApiResponseResult result = bjModelTypeService.getWorkCenterList("01", keyword,super.getPageRequest(sort));
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
    
    @ApiOperation(value = "检验工序与工作中心", notes = "检验工序与工作中心", hidden = true)
    @RequestMapping(value = "/doCheckInfo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doCheckInfo(@RequestBody Map<String, Object> params) throws Exception{
        String method = "basePrice/bjModelType/doCheckInfo";
        String methodName ="检验工序与工作中心";
        try{
        	String  input1 = params.get("input1")==null?"":params.get("input1").toString();
        	String  input2 = params.get("input2")==null?"":params.get("input2").toString();
        	ApiResponseResult result = bjModelTypeService.doCheckInfo("01",input1,input2,"","");
            logger.debug("检验工序与工作中心=doCheckInfo:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("检验工序与工作中心失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("检验工序与工作中心失败！");
        }
    }
}
