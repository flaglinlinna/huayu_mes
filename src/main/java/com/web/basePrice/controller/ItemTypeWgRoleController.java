package com.web.basePrice.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ItemTypeWgRole;
import com.web.basePrice.service.ItemTypeWgRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api("外购物料类型模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/itemTypeWgRole")
public class ItemTypeWgRoleController extends WebController{

    private String module = "外购物料类型";
    @Autowired
    private ItemTypeWgRoleService itemTypeWgRoleService;

    @ApiOperation(value = "外购物料类型表结构", notes = "外购物料类型表结构"+ ItemTypeWgRole.TABLE_NAME)
    @RequestMapping(value = "/getItemTypeWg", method = RequestMethod.GET)
    @ResponseBody
    public ItemTypeWgRole getItemTypeWg(){
        return new ItemTypeWgRole();
    }

    @ApiOperation(value = "外购物料类型列表页", notes = "外购物料类型列表页", hidden = true)
    @RequestMapping(value = "/toItemTypeWgRole")
    public String toItemTypeWg(){
        return "/web/basePrice/item_type_wg_role";
    }

    @ApiOperation(value = "获取外购物料类型列表", notes = "获取外购物料类型列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/itemTypeWgRole/getList";String methodName ="获取外购物料类型列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "wgId");
            ApiResponseResult result = itemTypeWgRoleService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取外购物料类型列表=getList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取外购物料类型失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取外购物料类型列表失败！");
        }
    }

    @ApiOperation(value = "新增外购物料类型信息", notes = "新增外购物料类型信息",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody ItemTypeWgRole itemTypeWg) {
        String method = "basePrice/itemTypeWgRole/add";String methodName ="新增外购物料类型信息";
        try{
            ApiResponseResult result = itemTypeWgRoleService.add(itemTypeWg);
            logger.debug("新增外购物料类型信息=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增外购物料类型新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("新增外购物料类型失败！");
        }
    }

    @ApiOperation(value = "编辑外购物料类型信息", notes = "编辑外购物料类型信息",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody ItemTypeWgRole itemTypeWg){
        String method = "basePrice/itemTypeWgRole/edit";String methodName ="编辑外购物料类型信息";
        try{
            ApiResponseResult result = itemTypeWgRoleService.edit(itemTypeWg);
            logger.debug("编辑外购物料类型信息=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑外购物料类型失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑外购物料类型失败！");
        }
    }

    @ApiOperation(value = "删除外购物料类型信息", notes = "删除外购物料类型信息",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/itemTypeWgRole/delete";String methodName ="删除外购物料类型信息";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = itemTypeWgRoleService.delete(id);
            logger.debug("删除外购物料类型信息=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除外购物料类型失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除外购物料类型失败！");
        }
    }

}
