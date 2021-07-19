package com.web.basePrice.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ProcRole;
import com.web.basePrice.service.ItemTypeWgRoleService;
import com.web.basePrice.service.ProcRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api("工艺关联角色模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/procRole")
public class ProcRoleController extends WebController{

    private String module = "工艺关联角色";
    @Autowired
    private ProcRoleService procRoleService;

    @ApiOperation(value = "工艺关联角色表结构", notes = "工艺关联角色表结构"+ ProcRole.TABLE_NAME)
    @RequestMapping(value = "/getItemTypeWg", method = RequestMethod.GET)
    @ResponseBody
    public ProcRole getItemTypeWg(){
        return new ProcRole();
    }

    @ApiOperation(value = "工艺关联角色列表页", notes = "工艺关联角色列表页", hidden = true)
    @RequestMapping(value = "/toProcRole")
    public String toItemTypeWg(){
        return "/web/basePrice/proc_role";
    }

    @ApiOperation(value = "获取工艺关联角色列表", notes = "获取工艺关联角色列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/ProcRole/getList";String methodName ="获取工艺关联角色列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "pkProc");
            ApiResponseResult result = procRoleService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取工艺关联角色列表=getList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺关联角色失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工艺关联角色列表失败！");
        }
    }

    @ApiOperation(value = "获取工艺关联角色列表", notes = "获取工艺关联角色列表",hidden = true)
    @RequestMapping(value = "/getListByWgId", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getListByWgId(Long wgId) {
        String method = "basePrice/ProcRole/getListByWgId";String methodName ="获取工艺关联角色列表";
        try {
            ApiResponseResult result = procRoleService.getByWgId(wgId);
            logger.debug("获取工艺关联角色列表=getList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取工艺关联角色失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取工艺关联角色列表失败！");
        }
    }

    @ApiOperation(value = "新增工艺关联角色信息", notes = "新增工艺关联角色信息",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody Map<String, Object> params) {
        String method = "basePrice/ProcRole/add";String methodName ="新增工艺关联角色信息";
        try{
            Long pkItemTypeWg = Long.parseLong(params.get("pkProc").toString());
            String roleIds = params.get("roleIds").toString();
            ApiResponseResult result = procRoleService.add(pkItemTypeWg,roleIds);
            logger.debug("新增工艺关联角色信息=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增工艺关联角色新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("新增工艺关联角色失败！");
        }
    }

    @ApiOperation(value = "编辑工艺关联角色信息", notes = "编辑工艺关联角色信息",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody ProcRole itemTypeWg){
        String method = "basePrice/ProcRole/edit";String methodName ="编辑工艺关联角色信息";
        try{
            ApiResponseResult result = procRoleService.edit(itemTypeWg);
            logger.debug("编辑工艺关联角色信息=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑工艺关联角色失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑工艺关联角色失败！");
        }
    }

    @ApiOperation(value = "删除工艺关联角色信息", notes = "删除工艺关联角色信息",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/ProcRole/delete";String methodName ="删除工艺关联角色信息";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = procRoleService.delete(id);
            logger.debug("删除工艺关联角色信息=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除工艺关联角色失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除工艺关联角色失败！");
        }
    }

}
