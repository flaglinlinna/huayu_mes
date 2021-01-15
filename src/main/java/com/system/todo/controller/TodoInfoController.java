package com.system.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 待办事项
 * @author fyx
 *
 */
@Api(description = "待办事项模块")
@RestController
@RequestMapping(value= "/todoInfo")
public class TodoInfoController extends WebController {
	@Autowired
	private TodoInfoService todoInfoService;
	
	@ApiOperation(value="新增待办事项", notes="新增待办事项")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ApiResponseResult add(TodoInfo todoInfo) {
		try {
			return todoInfoService.add(todoInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value="编辑待办事项", notes="编辑待办事项")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ApiResponseResult edit(TodoInfo todoInfo) {
		try {
			return todoInfoService.edit(todoInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value="关闭待办事项", notes="关闭待办事项")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Id", value = "ID（可传可不传）", dataType = "Long", paramType="query", defaultValue=""),
			@ApiImplicitParam(name = "bsUserId", value = "用户ID（可传可不传，传值时关联ID也必须传值）", dataType = "Long", paramType="query", defaultValue=""),
			@ApiImplicitParam(name = "roleId", value = "角色ID（可传可不传，传值时关联ID也必须传值）", dataType = "Long", paramType="query", defaultValue=""),
			@ApiImplicitParam(name = "bsReferId", value = "关联ID（可传可不传）", dataType = "Long", paramType="query", defaultValue="")
	})
	@RequestMapping(value = "/close", method = RequestMethod.GET)
	public ApiResponseResult close(Long Id, Long bsUserId, Long roleId, Long bsReferId) {
		try {
			return todoInfoService.close(Id, bsUserId, roleId, bsReferId);
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value="删除待办事项", notes="删除待办事项")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "ID", dataType = "Long", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ApiResponseResult delete(Long id) {
		try {
			if(null!=id && id==-1) {
				return ApiResponseResult.failure("没有删除权限");
			}
			return todoInfoService.delete(id);
		} catch (Exception e) {
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value="获取待办事项", notes="获取待办事项")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "bsStatus", value = "待办状态(0:未完成,1:已完成,2:已取消)", dataType = "int", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/getlist", method = RequestMethod.GET)
	public ApiResponseResult getlist(int bsStatus) {
		try {
			Sort sort = new Sort(Sort.Direction.DESC,"id");
			return todoInfoService.getlist(bsStatus, super.getPageRequest(sort));
		} catch (Exception e) {
			return ApiResponseResult.failure(e.getMessage());
		}
	}
	
	@ApiOperation(value="获取待办事项", notes="获取待办事项")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "keyword", value = "查询关键字", dataType = "String", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/getlist2", method = RequestMethod.GET)
	public ApiResponseResult getlist2(String keyword) {
		try {
			Sort sort = new Sort(Sort.Direction.DESC,"id");
			return todoInfoService.getlist2(keyword, super.getPageRequest(sort));
		} catch (Exception e) {
			return ApiResponseResult.failure(e.getMessage());
		}
	}
}

