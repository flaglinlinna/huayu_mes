package com.web.basic.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.LinerImg;
import com.web.basic.service.LinerImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(description = "组长铁三角信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/linerImg")
public class LinerImgController extends WebController{
	
	private String module = "组长铁三角信息";

	 @Autowired
	 private LinerImgService linerImgService;
	 
	 	@ApiOperation(value = "组长铁三角列表页", notes = "组长铁三角列表页", hidden = true)
	    @RequestMapping(value = "/toLinerImg")
	    public String toLine(){
	        return "/web/basic/linerImg";
	    }

//	    @ApiOperation(value = "获取组长铁三角列表", notes = "获取组长铁三角列表", hidden = true)
//	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
//	    @ResponseBody
////	    public ApiResponseResult getList(String keyword) {
////	        String method = "base/linerImg/getList";String methodName ="获取组长铁三角列表";
////	        try {
////	            Sort sort =  Sort.unsorted();
////	            ApiResponseResult result = linerImgService.getList(keyword, super.getPageRequest(sort));
////	            logger.debug("获取组长铁三角列表=getList:");
////	            return result;
////	        } catch (Exception e) {
////	            e.printStackTrace();
////	            logger.error("获取组长铁三角列表失败！", e);
////				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
////	            return ApiResponseResult.failure("获取组长铁三角列表失败！");
////	        }
////	    }

		@ApiOperation(value = "获取组长铁三角列表", notes = "获取组长铁三角列表", hidden = true)
		@RequestMapping(value = "/getList", method = RequestMethod.GET)
		@ResponseBody
	public ApiResponseResult getReport(@RequestParam(value = "dates", required = false) String dates,
									   @RequestParam(value = "keyword", required = false) String keyword) {
			String method = "base/linerImg/getList";String methodName ="获取组长铁三角列表";
		String param = "日期:"+dates ;
		try {
			String[] date = {"",""};
			if(StringUtils.isNotEmpty(dates)){
				date = dates.split(" - ");
			}
			Sort sort = Sort.unsorted();
			ApiResponseResult result = linerImgService.getList(date[0],date[1], keyword,super.getPageRequest(sort));
			logger.debug(methodName+"=getReport:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, param+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}
	    
	    
	    @ApiOperation(value = "新增组长铁三角", notes = "新增组长铁三角", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody LinerImg linerImg) {
	        String method = "base/linerImg/add";String methodName ="新增组长铁三角";
	        try{
	            ApiResponseResult result = linerImgService.add(linerImg);
	            logger.debug("新增组长铁三角=add:");
	            getSysLogService().success(module,method, methodName, linerImg.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("组长铁三角新增失败！", e);
	            getSysLogService().error(module,method, methodName, linerImg.toString()+","+e.toString());
	            return ApiResponseResult.failure("组长铁三角新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑组长铁三角", notes = "编辑组长铁三角", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody LinerImg linerImg){
	        String method = "base/linerImg/edit";String methodName ="编辑组长铁三角";
	        try{
	            ApiResponseResult result = linerImgService.edit(linerImg);
	            logger.debug("编辑组长铁三角=edit:");
	            getSysLogService().success(module,method, methodName, linerImg.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑组长铁三角失败！", e);
	            getSysLogService().error(module,method, methodName, linerImg.toString()+","+e.toString());
	            return ApiResponseResult.failure("编辑组长铁三角失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取组长铁三角", notes = "根据ID获取组长铁三角", hidden = true)
	    @RequestMapping(value = "/getLinerImg", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getLine(@RequestBody Map<String, Object> params){
	        String method = "base/linerImg/getLine";String methodName ="根据ID获取组长铁三角";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = linerImgService.getLinerImg(id);
	            logger.debug("根据ID获取组长铁三角=getLine:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取组长铁三角失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("获取组长铁三角失败！");
	        }
	    }
		
		@ApiOperation(value = "删除组长铁三角", notes = "删除组长铁三角", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/linerImg/delete";String methodName ="删除组长铁三角";
	        try{
	        	String ids = params.get("id").toString() ;
	            ApiResponseResult result = linerImgService.delete(ids);
	            logger.debug("删除组长铁三角=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除组长铁三角失败！", e);
	            getSysLogService().error(module,method, methodName, params+","+e.toString());
	            return ApiResponseResult.failure("删除组长铁三角失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/linerImg/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
		            ApiResponseResult result = linerImgService.doStatus(id, bsStatus);
		            logger.debug("设置正常/禁用=doJob:");
		            getSysLogService().success(module,method, methodName, params);
		            return result;
		        }catch (Exception e){
		            e.printStackTrace();
		            logger.error("设置正常/禁用失败！", e);
		            getSysLogService().error(module,method, methodName, params+";"+e.toString());
		            return ApiResponseResult.failure("设置正常/禁用失败！");
		        }
		    }

    @ApiOperation(value = "获取部门信息", notes = "获取部门信息", hidden = true)
    @RequestMapping(value = "/getDeptInfo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getDeptInfo(String keyword) {
        String method = "base/linerImg/getDeptInfo";
        String methodName = "获取部门信息";
        try {
            ApiResponseResult result = linerImgService.getDeptInfo(keyword);
            logger.debug("获取部门信息=getDeptInfo:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取部门信息失败！", e);
            getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
            return ApiResponseResult.failure("获取部门信息失败！");
        }
    }

	@ApiOperation(value="获取员工信息", notes="获取员工信息", hidden = true)
	@RequestMapping(value = "/getEmpCode", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getEmpCode(String keyword) {
		String method = "base/linerImg/getEmpCode";String methodName ="获取员工信息";
		try {
			Sort sort = Sort.unsorted();
			ApiResponseResult result = linerImgService.getEmpCode(keyword,super.getPageRequest(sort));
			logger.debug("获取员工信息=getEmpCode:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取员工信息失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取员工信息失败！");
		}
	}

	@ApiOperation(value="获取生产线信息", notes="获取生产线信息", hidden = true)
	@RequestMapping(value = "/getLine", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getLine() {
		String method = "base/linerImg/getLine";String methodName ="获取生产线信息";
		try {
			ApiResponseResult result = linerImgService.getLine();
			logger.debug("获取生产线信息=getLine:");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产线信息失败！", e);
			getSysLogService().error(module,method, methodName,e.toString());
			return ApiResponseResult.failure("获取生产线信息失败！");
		}
	}

}
