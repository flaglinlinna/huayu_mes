package com.email.service;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.app.base.data.ApiResponseResult;
import com.system.file.entity.FsFile;

/**
 * 文件管理
 * @author fyx
 *
 */
public interface EmailService {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final Charset CHARSET = Charset.forName("UTF-8");

	public ApiResponseResult upload(FsFile fsFile, MultipartFile file) throws Exception;

	public ApiResponseResult get(Long fsFileId, HttpServletResponse response) throws Exception;

	public ApiResponseResult onlineView(Long fsFileId, HttpServletResponse response) throws Exception;

	public ApiResponseResult delete(Long fsFileId) throws Exception;

	//发送邮件测试
	public ApiResponseResult test() throws Exception;

}
