package com.web.basic.controller;

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
import com.web.basic.entity.Defective;
import com.web.basic.entity.Mtrial;
import com.web.basic.service.MtrialService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "物料信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/mtrial")
public class MtrialController extends WebController{

	private String module = "物料信息";

	 @Autowired
	 private MtrialService mtrialService;
	 
	 @ApiOperation(value = "物料基础信息表结构", notes = "物料基础信息表结构"+Mtrial.TABLE_NAME)
	    @RequestMapping(value = "/getMtrial", method = RequestMethod.GET)
		@ResponseBody
	    public Mtrial getMtrial(){
	        return new Mtrial();
	    }
	 
	 @ApiOperation(value = "物料信息列表页", notes = "物料信息列表页", hidden = true)
	    @RequestMapping(value = "/toMtrial")
	    public String toMtrial(){
	        return "/web/basic/mtrial";
	    }
	    @ApiOperation(value = "获取物料信息列表", notes = "获取物料信息列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/mtrial/getList";String methodName ="获取物料信息列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.ASC, "itemNo");
	            ApiResponseResult result = mtrialService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取物料信息列表=getList:");
	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取物料信息列表失败！", e);
	            getSysLogService().error(module,method, methodName,keyword+";"+ e.toString());
	            return ApiResponseResult.failure("获取物料信息列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增物料信息", notes = "新增物料信息", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Mtrial mtrial) {
	        String method = "base/mtrial/add";String methodName ="新增物料信息";
	        try{
	            ApiResponseResult result = mtrialService.add(mtrial);
	            logger.debug("新增物料信息=add:");
	            getSysLogService().success(module,method, methodName, mtrial.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("物料信息新增失败！", e);
	            getSysLogService().error(module,method, methodName,mtrial.toString()+":"+ e.toString());
	            return ApiResponseResult.failure("物料信息新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑物料信息", notes = "编辑物料信息", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Mtrial mtrial){
	        String method = "base/mtrial/edit";String methodName ="编辑物料信息";
	        try{
	            ApiResponseResult result = mtrialService.edit(mtrial);
	            logger.debug("编辑物料信息=edit:");
	            getSysLogService().success(module,method, methodName, mtrial.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑物料信息失败！", e);
	            getSysLogService().error(module,method, methodName,mtrial.toString()+":"+ e.toString());
	            return ApiResponseResult.failure("编辑物料信息失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取物料", notes = "根据ID获取物料", hidden = true)
	    @RequestMapping(value = "/getMtrial", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getMtrial(@RequestBody Map<String, Object> params){
	        String method = "base/mtrial/getMtrial";String methodName ="根据ID获取物料";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = mtrialService.getMtrial(id);
	            logger.debug("根据ID获取物料=getMtrial:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取物料失败！", e);
	            getSysLogService().error(module,method, methodName, params+":"+e.toString());
	            return ApiResponseResult.failure("获取物料失败！");
	        }
	    }
		
		@ApiOperation(value = "删除物料", notes = "删除物料", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/mtrial/delete";String methodName ="删除物料";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = mtrialService.delete(id);
	            logger.debug("删除物料=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除物料失败！", e);
	            getSysLogService().error(module,method, methodName,params+":"+ e.toString());
	            return ApiResponseResult.failure("删除物料失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/mtrial/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
		            ApiResponseResult result = mtrialService.doStatus(id, bsStatus);
		            logger.debug("设置正常/禁用=doJob:");
		            getSysLogService().success(module,method, methodName, params);
		            return result;
		        }catch (Exception e){
		            e.printStackTrace();
		            logger.error("设置正常/禁用失败！", e);
		            getSysLogService().error(module,method, methodName,params+":"+ e.toString());
		            return ApiResponseResult.failure("设置正常/禁用失败！");
		        }
		    }
}
