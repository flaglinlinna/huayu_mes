package com.web.po.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.po.entity.DatabaseInfo;
import com.web.po.service.DatabaseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Item预测协同模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/api/database")
public class DatabaseController extends WebController {

	 
    @Autowired
	private DatabaseService databaseService;

	@ApiOperation(value = "DatabaseList页", notes = "DatabaseList页", hidden = true)
	@RequestMapping(value = "/toDatabaseList")
	public String toDatabaseList(){
	    return "/api/database/database_list";
	}
	
	@ApiOperation(value = "DatabaseAdd页", notes = "DatabaseAdd页", hidden = true)
	@RequestMapping(value = "/toDatabaseAdd")
	public String toDatabaseAdd(){
	    return "/api/database/database_add";
	}

	@PostMapping(value = "/add")
	@ApiOperation(value = "新增")
	@ResponseBody
	public ApiResponseResult add(@RequestBody DatabaseInfo databaseInfo){
	    try {
			return databaseService.add(databaseInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ApiResponseResult.failure(e.toString());
		}
	}
	
	/*@PostMapping(value = "/getlist")
	@ApiOperation(value = "获取配置列表")
	@ResponseBody
	public ApiResponseResult getlist(@RequestBody Map<String, Object> params){
	    try {
	    	String keyword = params.get("keyword").toString();
	    	Sort sort = new Sort(Sort.Direction.DESC, "id");
			return databaseService.getlist(keyword,super.getPageRequest(sort));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ApiResponseResult.failure(e.toString());
		}
	}*/
	@ApiOperation(value = "获取配置列表", notes = "获取配置列表")
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getlist(@RequestParam(value = "keyword", required = false) String keyword) {
        String method = "/getlist";String methodName ="获取配置列表";
        try {
        	Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = databaseService.getlist(keyword,  super.getPageRequest(sort));
            logger.debug("获取配置列表=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取配置列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取配置列表失败！");
        }
    }
	
	@PostMapping(value = "/testconnection")
	@ApiOperation(value = "获取Item预测协同信息")
	@ResponseBody
	public ApiResponseResult testConnection(@RequestBody Map<String, Object> params){
	    try {
	    	String type = params.get("type").toString();
	    	String url = params.get("url").toString();
	    	String username = params.get("username").toString();
	    	String password = params.get("password").toString();
			return databaseService.testConnection(type,url,username,password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ApiResponseResult.failure(e.toString());
		}
	}
	
	
    
}
