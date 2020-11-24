package com.web.basic.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ProdErr;
import com.web.basic.service.ProdErrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "生产异常原因信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/proderr")
public class ProdErrController extends WebController{

	private String module = "生产异常原因信息";

	 @Autowired
	 private ProdErrService prodErrService;

	 @ApiOperation(value = "生产异常原因表结构", notes = "生产异常原因表结构"+ProdErr.TABLE_NAME)
	    @RequestMapping(value = "/getProdErr", method = RequestMethod.GET)
		@ResponseBody
	    public ProdErr getProdErr(){
	        return new ProdErr();
	    }


	 @ApiOperation(value = "生产异常原因列表页", notes = "生产异常原因列表页", hidden = true)
	    @RequestMapping(value = "/toProdErr")
	    public String toProdErr(){
	        return "/web/basic/proderr";
	    }
	    @ApiOperation(value = "获取生产异常原因列表", notes = "获取生产异常原因列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "base/proderr/getList";String methodName ="获取生产异常原因列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = prodErrService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取生产异常原因列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取生产异常原因列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取生产异常原因列表失败！");
	        }
	    }


	    @ApiOperation(value = "新增生产异常原因", notes = "新增生产异常原因", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody ProdErr prodErr) {
	        String method = "base/proderr/add";String methodName ="新增生产异常原因";
	        try{
	            ApiResponseResult result = prodErrService.add(prodErr);
	            logger.debug("新增生产异常原因=add:");
	            getSysLogService().success(module,method, methodName, prodErr.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("生产异常原因新增失败！", e);
	            getSysLogService().error(module,method, methodName, prodErr.toString()+";"+e.toString());
	            return ApiResponseResult.failure("生产异常原因新增失败！");
	        }
	    }

	    @ApiOperation(value = "编辑生产异常原因", notes = "编辑生产异常原因", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody ProdErr prodErr){
	        String method = "base/proderr/edit";String methodName ="编辑生产异常原因";
	        try{
	            ApiResponseResult result = prodErrService.edit(prodErr);
	            logger.debug("编辑生产异常原因=edit:");
	            getSysLogService().success(module,method, methodName, prodErr.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑生产异常原因失败！", e);
	            getSysLogService().error(module,method, methodName, prodErr.toString()+";"+e.toString());
	            return ApiResponseResult.failure("编辑生产异常原因失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取生产异常原因", notes = "根据ID获取生产异常原因", hidden = true)
	    @RequestMapping(value = "/getProdErr", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getProdErr(@RequestBody Map<String, Object> params){
	        String method = "base/proderr/getProdErr";String methodName ="根据ID获取生产异常原因";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = prodErrService.getProdErr(id);
	            logger.debug("根据ID获取生产异常原因=getProdErr:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取生产异常原因失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("获取生产异常原因失败！");
	        }
	    }

		@ApiOperation(value = "删除生产异常原因", notes = "删除生产异常原因", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/proderr/delete";String methodName ="删除生产异常原因";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = prodErrService.delete(id);
	            logger.debug("删除生产异常原因=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除生产异常原因失败！", e);
	            getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	            return ApiResponseResult.failure("删除生产异常原因失败！");
	        }
	    }

}
