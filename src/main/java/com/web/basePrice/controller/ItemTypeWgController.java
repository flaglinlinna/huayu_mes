package com.web.basePrice.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.service.ItemTypeWgService;
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
@RequestMapping(value = "basePrice/itemTypeWg")
public class ItemTypeWgController extends WebController{

    private String module = "外购物料类型";
    @Autowired
    private ItemTypeWgService itemTypeWgService;

    @ApiOperation(value = "外购物料类型表结构", notes = "外购物料类型表结构"+ ItemTypeWg.TABLE_NAME)
    @RequestMapping(value = "/getItemTypeWg", method = RequestMethod.GET)
    @ResponseBody
    public ItemTypeWg getItemTypeWg(){
        return new ItemTypeWg();
    }

    @ApiOperation(value = "外购物料类型列表页", notes = "外购物料类型列表页", hidden = true)
    @RequestMapping(value = "/toItemTypeWg")
    public String toItemTypeWg(){
        return "/web/basePrice/item_type_wg";
    }

    @ApiOperation(value = "获取外购物料类型列表", notes = "获取外购物料类型列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/itemTypeWg/getList";String methodName ="获取外购物料类型列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = itemTypeWgService.getList(keyword, super.getPageRequest(sort));
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
    public ApiResponseResult add(@RequestBody ItemTypeWg itemTypeWg) {
        String method = "basePrice/profitProd/add";String methodName ="新增外购物料类型信息";
        try{
            ApiResponseResult result = itemTypeWgService.add(itemTypeWg);
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
    public ApiResponseResult edit(@RequestBody ItemTypeWg itemTypeWg){
        String method = "basePrice/profitProd/edit";String methodName ="编辑外购物料类型信息";
        try{
            ApiResponseResult result = itemTypeWgService.edit(itemTypeWg);
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
        String method = "basePrice/profitProd/delete";String methodName ="删除外购物料类型信息";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = itemTypeWgService.delete(id);
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
