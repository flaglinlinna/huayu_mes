package com.web.basic.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.Abnormal;
import com.web.basic.service.AbnormalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "异常类别信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/abnormal")
public class AbnormalController extends WebController{

	private String module = "异常类别信息";

	 @Autowired
	 private AbnormalService abnormalService;

	 @ApiOperation(value = "异常类别表结构", notes = "异常类别表结构"+Abnormal.TABLE_NAME)
	    @RequestMapping(value = "/getAbnormal", method = RequestMethod.GET)
		@ResponseBody
	    public Abnormal getAbnormal(){
	        return new Abnormal();
	    }


	 @ApiOperation(value = "异常类别列表页", notes = "异常类别列表页", hidden = true)
	    @RequestMapping(value = "/toAbnormal")
	    public String toAbnormal(){
	        return "/web/basic/abnormal";
	    }
	    @ApiOperation(value = "获取异常类别列表", notes = "获取异常类别列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/abnormal/getList";String methodName ="获取异常类别列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = abnormalService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取异常类别列表=getList:");
	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取异常类别列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取异常类别列表失败！");
	        }
	    }


	    @ApiOperation(value = "新增异常类别", notes = "新增异常类别", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Abnormal abnormal) {
	        String method = "base/abnormal/add";String methodName ="新增异常类别";
	        try{
	            ApiResponseResult result = abnormalService.add(abnormal);
	            logger.debug("新增异常类别=add:");
	            getSysLogService().success(module,method, methodName, abnormal.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("异常类别新增失败！", e);
	            getSysLogService().error(module,method, methodName, abnormal.toString()+";"+e.toString());
	            return ApiResponseResult.failure("异常类别新增失败！");
	        }
	    }

	    @ApiOperation(value = "编辑异常类别", notes = "编辑异常类别", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Abnormal abnormal){
	        String method = "base/abnormal/edit";String methodName ="编辑异常类别";
	        try{
	            ApiResponseResult result = abnormalService.edit(abnormal);
	            logger.debug("编辑异常类别=edit:");
	            getSysLogService().success(module,method, methodName, abnormal.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑异常类别失败！", e);
	            getSysLogService().error(module,method, methodName, abnormal.toString()+";"+e.toString());
	            return ApiResponseResult.failure("编辑异常类别失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取异常类别", notes = "根据ID获取异常类别", hidden = true)
	    @RequestMapping(value = "/getAbnormal", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getAbnormal(@RequestBody Map<String, Object> params){
	        String method = "base/abnormal/getAbnormal";String methodName ="根据ID获取异常类别";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = abnormalService.getAbnormal(id);
	            logger.debug("根据ID获取异常类别=getAbnormal:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取异常类别失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("获取异常类别失败！");
	        }
	    }

		@ApiOperation(value = "删除异常类别", notes = "删除异常类别", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/abnormal/delete";String methodName ="删除异常类别";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = abnormalService.delete(id);
	            logger.debug("删除异常类别=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除异常类别失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("删除异常类别失败！");
	        }
	    }

}
