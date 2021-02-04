package com.system.todo.service;

import java.nio.charset.Charset;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.system.todo.entity.TodoInfo;

/**
 * 待办事项
 * @author fyx
 *
 */
public interface TodoInfoService {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final Charset CHARSET = Charset.forName("UTF-8");

	public ApiResponseResult add(TodoInfo todoInfo) throws Exception;

	public ApiResponseResult edit(TodoInfo todoInfo) throws Exception;

	public ApiResponseResult close(Long id, Long bsUserId, Long roleId, Long bsReferId) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getlist(int bsStatus, PageRequest pageRequest) throws Exception;
	
	public ApiResponseResult getlist2(String keyword,String bsStatus,PageRequest pageRequest)throws Exception;//20210114-lst
	
	public ApiResponseResult closeByIdAndModel(Long bsReferId,String model) throws Exception;
}
