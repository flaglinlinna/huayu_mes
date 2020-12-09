package com.web.basePrice.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ProfitProd;
import com.web.basePrice.service.ProfitProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api("产品利润率信息维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/profitProd")
public class ProfitProdController extends WebController{

    private String module = "产品利润率信息维护";
    @Autowired
    private ProfitProdService profitProdService;

    @ApiOperation(value = "产品利润率信息维护表结构", notes = "产品利润率信息维护结构"+ ProfitProd.TABLE_NAME)
    @RequestMapping(value = "/getProfitProd", method = RequestMethod.GET)
    @ResponseBody
    public ProfitProd getProfitProd(){
        return new ProfitProd();
    }

    @ApiOperation(value = "产品利润率信息维护列表页", notes = "产品利润率信息列表页", hidden = true)
    @RequestMapping(value = "/toProfitProd")
    public String toProfitProd(){
        return "/web/basePrice/profit_prod";
    }

    @ApiOperation(value = "获取产品利润率信息列表", notes = "获取产品利润率信息列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword) {
        String method = "basePrice/profitProd/getList";String methodName ="获取产品利润率信息列表";
        try {
            System.out.println(keyword);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = profitProdService.getList(keyword, super.getPageRequest(sort));
            logger.debug("获取产品利润率信息列表=getList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取产品利润率信息列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取产品利润率信息列表失败！");
        }
    }

    @ApiOperation(value = "新增产品利润率信息", notes = "新增产品利润率信息",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody ProfitProd profitProd) {
        String method = "basePrice/profitProd/add";String methodName ="新增产品利润率信息";
        try{
            ApiResponseResult result = profitProdService.add(profitProd);
            logger.debug("新增产品利润率信息=add:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增产品利润率信息新增失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("新增产品利润率信息新增失败！");
        }
    }

    @ApiOperation(value = "编辑产品利润率信息", notes = "编辑产品利润率信息",hidden = true)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(@RequestBody ProfitProd profitProd){
        String method = "basePrice/profitProd/edit";String methodName ="编辑产品利润率信息";
        try{
            ApiResponseResult result = profitProdService.edit(profitProd);
            logger.debug("编辑产品利润率信息=edit:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("编辑产品利润率信息失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("编辑产品利润率信息失败！");
        }
    }

    @ApiOperation(value = "删除产品利润率信息", notes = "删除产品利润率信息",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/profitProd/delete";String methodName ="删除产品利润率信息";
        try{
            long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = profitProdService.delete(id);
            logger.debug("删除产品利润率信息=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除产品利润率信息失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除产品利润率信息失败！");
        }
    }
    
    @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
	 //Long id, Integer deStatus
        String method = "basePrice/profitProd/doStatus";String methodName ="设置正常/禁用";
        try{
        	long id = Long.parseLong(params.get("id").toString()) ;
        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
            ApiResponseResult result = profitProdService.doStatus(id, bsStatus);
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
