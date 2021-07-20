package com.system.wechat.service;

import java.nio.charset.Charset;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.system.todo.entity.TodoInfo;

/**
 * 企业wechat信息设置
 * @author hjj
 *
 */
public interface WechatSettingService {


	public ApiResponseResult getToken() throws Exception;

	//获得企业微信部门列表
	public ApiResponseResult getDepartment() throws Exception;

	//获得企业微信部门列表
	public ApiResponseResult getUserByDept(String DepartId) throws Exception;
}
