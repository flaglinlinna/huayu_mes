package com.web.basic.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ItemsTime;
import com.web.basic.service.ItemTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "静置时间维护信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/itemsTime")
public class ItemsTimeController extends WebController{

	private String module = "静置时间维护信息";

	 @Autowired
	 private ItemTimeService itemTimeService;

	 @ApiOperation(value = "生产异常原因表结构", notes = "生产异常原因表结构"+ItemsTime.TABLE_NAME)
	    @RequestMapping(value = "/getItemsTime", method = RequestMethod.GET)
		@ResponseBody
	    public ItemsTime getItemsTime(){
	        return new ItemsTime();
	    }


	 @ApiOperation(value = "静置时间维护信息列表页", notes = "静置时间维护信息列表页", hidden = true)
	    @RequestMapping(value = "/toItemsTime")
	    public String toProdErr(){
	        return "/web/basic/items_time";
	    }
	    @ApiOperation(value = "获取静置时间维护信息列表", notes = "获取静置时间维护信息列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/itemsTime/getList";String methodName ="获取静置时间维护信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = itemTimeService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取静置时间维护信息列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取静置时间维护信息列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取静置时间维护信息列表失败！");
	        }
	    }



	@ApiOperation(value = "获取静置时间物料信息选择", notes = "获取静置时间物料信息选择", hidden = true)
	@RequestMapping(value = "/getItemsList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getItemsList(String keyword) {
		String method = "base/itemsTime/getItemsList";String methodName ="获取静置时间物料信息选择";
		try {
			System.out.println(keyword);
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			ApiResponseResult result = itemTimeService.getItemSelect(keyword, super.getPageRequest(sort));
			logger.debug("获取静置时间物料信息选择=getItemsList:");
//	            getSysLogService().success(module,method, methodName, keyword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取静置时间物料信息选择失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取静置时间物料信息选择失败！");
		}
	}

	    @ApiOperation(value = "新增静置时间维护信息", notes = "新增静置时间维护信息", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody ItemsTime itemsTime) {
	        String method = "base/itemsTime/add";String methodName ="新增静置时间维护信息";
	        try{
	            ApiResponseResult result = itemTimeService.add(itemsTime);
	            logger.debug("新增静置时间维护信息=add:");
	            getSysLogService().success(module,method, methodName, itemsTime.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("静置时间维护信息新增失败！", e);
	            getSysLogService().error(module,method, methodName, itemsTime.toString()+";"+e.toString());
	            return ApiResponseResult.failure("静置时间维护信息新增失败！");
	        }
	    }

	    @ApiOperation(value = "编辑静置时间维护信息", notes = "编辑静置时间维护信息", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody ItemsTime itemsTime){
	        String method = "base/itemsTime/edit";String methodName ="编辑静置时间维护信息";
	        try{
	            ApiResponseResult result = itemTimeService.edit(itemsTime);
	            logger.debug("编辑静置时间维护信息=edit:");
	            getSysLogService().success(module,method, methodName, itemsTime.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑静置时间维护信息失败！", e);
	            getSysLogService().error(module,method, methodName, itemsTime.toString()+";"+e.toString());
	            return ApiResponseResult.failure("编辑静置时间维护信息失败！");
	        }
	    }


		@ApiOperation(value = "删除静置时间维护信息", notes = "删除静置时间维护信息", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/itemsTime/delete";String methodName ="删除静置时间维护信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = itemTimeService.delete(id);
	            logger.debug("删除静置时间维护信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除静置时间维护信息失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("删除静置时间维护信息失败！");
	        }
	    }

}
