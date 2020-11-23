package com.web.basic.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.AppVersion;
import com.web.basic.service.AppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "APP版本更新")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/app")
public class AppController extends WebController{
	
	private String module = "APP版本更新";

	 @Autowired
	 private AppService appService;
	 
	 @ApiOperation(value = "APP版本更新页", notes = "APP版本更新页", hidden = true)
	    @RequestMapping(value = "/toApp")
	    public String toLine(){
	        return "/web/app/app";
	    }
	    @ApiOperation(value = "获取APP版本更新列表", notes = "获取APP版本更新列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "app/getList";String methodName ="获取APP版本更新列表";
	        String param = "关键字:"+keyword;
	        try {
	        	System.out.println(param);
	            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
	            ApiResponseResult result = appService.getList(keyword, super.getPageRequest(sort));
	            logger.debug(methodName+"=getList:");
	            getSysLogService().success(module,method, methodName, param);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error(methodName+"失败！", e);
	            getSysLogService().error(module,method, methodName, param+";"+e.toString());
	            return ApiResponseResult.failure(methodName+"失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增线体", notes = "新增线体", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody AppVersion app) {
	        String method = "base/line/add";String methodName ="新增线体";
	        try{
	            ApiResponseResult result = appService.add(app);
	            logger.debug("新增线体=add:");
	            getSysLogService().success(module,method, methodName, app.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("线体新增失败！", e);
	            getSysLogService().error(module,method, methodName, app.toString()+","+e.toString());
	            return ApiResponseResult.failure("线体新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑线体", notes = "编辑线体", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody AppVersion app){
	        String method = "base/line/edit";String methodName ="编辑线体";
	        try{
	            ApiResponseResult result = appService.edit(app);
	            logger.debug("编辑线体=edit:");
	            getSysLogService().success(module,method, methodName, app.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑线体失败！", e);
	            getSysLogService().error(module,method, methodName, app.toString()+","+e.toString());
	            return ApiResponseResult.failure("编辑线体失败！");
	        }
	    }

		
		@ApiOperation(value = "删除线体", notes = "删除线体", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/line/delete";String methodName ="删除线体";
	        try{
	        	String ids = params.get("id").toString() ;
	            ApiResponseResult result = appService.delete(ids);
	            logger.debug("删除线体=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除线体失败！", e);
	            getSysLogService().error(module,method, methodName, params+","+e.toString());
	            return ApiResponseResult.failure("删除线体失败！");
	        }
	    }
		
		@ApiOperation(value="上传文件", notes="上传文件")
		@ApiImplicitParams({
				@ApiImplicitParam(name = "file", value = "附件", dataType = "MultipartFile", paramType="query",defaultValue=""),
		})
		@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
		public ApiResponseResult upload(MultipartFile file) {
			try {
				return appService.upload(file);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				return ApiResponseResult.failure(e.getMessage());
			}
		}

}
