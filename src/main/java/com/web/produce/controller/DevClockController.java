package com.web.produce.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.DevClock;
import com.web.produce.service.DevClockService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(description = "卡机信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "produce/dev_clock")
public class DevClockController extends WebController{

	 private String module = "卡机信息";
	 @Autowired
	 private DevClockService devClockService;
	 
	 @ApiOperation(value = "卡机基础信息表结构", notes = "卡机基础信息表结构"+DevClock.TABLE_NAME)
	    @RequestMapping(value = "/getDevClock", method = RequestMethod.GET)
		@ResponseBody
	    public DevClock getDevClock(){
	        return new DevClock();
	    }
	 
	 
	 @ApiOperation(value = "卡机信息列表页", notes = "卡机信息列表页", hidden = true)
	    @RequestMapping(value = "/toDevClock")
	    public String toDevClock(){
	        return "/web/produce/dev_clock/dev_clock";
	    }
	 
	    @ApiOperation(value = "获取卡机信息列表", notes = "获取卡机信息列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "produce/dev_clock/getList";String methodName ="获取卡机信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = devClockService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取卡机信息列表=getList:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取卡机信息列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取卡机信息列表失败！");
	        }
	    }


	@ApiOperation(value = "导出卡机信息", notes = "导出卡机信息",hidden = true)
	@RequestMapping(value = "/exportList", method = RequestMethod.GET)
	@ResponseBody
	public void exportList(String keyword) {
		String method = "produce/dev_clock/exportList";String methodName ="导出卡机信息";
		try {
			System.out.println(keyword);
			devClockService.exportList(keyword,getResponse());
			logger.debug("导出卡机信息=exportList:");
			getSysLogService().success(module,method, methodName, "关键字:"+keyword);
//			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出卡机信息！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
//			return ApiResponseResult.failure("导出卡机信息！");
		}
	}
	    
	    
	    @ApiOperation(value = "新增卡机信息", notes = "新增卡机信息",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody DevClock devClock) {
	        String method = "produce/dev_clock/add";String methodName ="新增卡机信息";
	        try{
	            ApiResponseResult result = devClockService.add(devClock);
	            logger.debug("新增卡机信息=add:");
	            getSysLogService().success(module,method, methodName, devClock.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("卡机信息新增失败！", e);
	            getSysLogService().error(module,method, methodName,  devClock.toString()+";"+e.toString());
	            return ApiResponseResult.failure("卡机信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑卡机信息", notes = "编辑卡机信息",hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody DevClock devClock){
	        String method = "produce/dev_clock/edit";String methodName ="编辑卡机信息";
	        try{
	            ApiResponseResult result = devClockService.edit(devClock);
	            logger.debug("编辑卡机信息=edit:");
	            getSysLogService().success(module,method, methodName, devClock.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑卡机信息失败！", e);
	            getSysLogService().error(module,method, methodName,devClock.toString() +";"+e.toString());
	            return ApiResponseResult.failure("编辑卡机信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取卡机信息", notes = "根据ID获取卡机信息",hidden = true)
	    @RequestMapping(value = "/getDevClock", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getDevClock(@RequestBody Map<String, Object> params){
	        String method = "produce/dev_clock/getDevClock";String methodName ="根据ID获取卡机信息";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = devClockService.getDevClock(id);
	            logger.debug("根据ID获取卡机信息=getDevClock:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取卡机信息失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("获取卡机信息失败！");
	        }
	    }
		
		@ApiOperation(value = "删除卡机信息", notes = "删除卡机信息",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "produce/dev_clock/delete";String methodName ="删除卡机信息";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = devClockService.delete(id);
	            logger.debug("删除卡机信息=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除卡机信息失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("删除卡机信息失败！");
	        }
	    }
		
		
		@ApiOperation(value = "设置有效/无效", notes = "设置有效/无效", hidden = true)
	    @RequestMapping(value = "/doEnabled", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult doEnabled(@RequestBody Map<String, Object> params) throws Exception{
		 //Long id, Integer deStatus
	        String method = "produce/dev_clock/doEnabled";String methodName ="设置有效/无效";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	        	Integer bsStatus=Integer.parseInt(params.get("enabled").toString());
	            ApiResponseResult result = devClockService.doEnabled(id, bsStatus);
	            logger.debug("设置有效/无效=doJob:");
	            getSysLogService().success(module,method, methodName, "设置id:"+id+ (bsStatus==0?"有效":"无效"));
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("设置有效/无效！", e);
	            getSysLogService().error(module,method, methodName, "设置有效/无效失败！"+e.toString());
	            return ApiResponseResult.failure("设置有效/无效失败！");
	        }
	    }
		
		@ApiOperation(value = "获取线体信息列表", notes = "获取线体信息列表", hidden = true)
	    @RequestMapping(value = "/getLineList", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getLineList() {
	        String method = "produce/dev_clock/getLineList";String methodName ="获取线体信息列表";
	        try {
	            ApiResponseResult result = devClockService.getLineList();
	            logger.debug("获取线体信息列表=getLineList:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取线体信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取线体信息列表失败！");
	        }
	    }
		
		 @ApiOperation(value = "测试卡机连接", notes = "测试卡机连接",hidden = true)
		    @RequestMapping(value = "/test", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult test(@RequestBody DevClock devClock) {
		        String method = "produce/dev_clock/test";String methodName ="测试卡机连接";
		        try{
		            ApiResponseResult result = devClockService.test(devClock);
		            logger.debug("测试卡机连接=test:");
		            getSysLogService().success(module,method, methodName, null);
		            return result;
		        }catch(Exception e){
		            e.printStackTrace();
		            logger.error("测试卡机连接失败！", e);
		            getSysLogService().error(module,method, methodName, e.toString());
		            return ApiResponseResult.failure("测试卡机连接失败！");
		        }
		    }
}
