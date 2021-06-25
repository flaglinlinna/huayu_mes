package com.system.file.service;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import com.app.base.data.ApiResponseResult;
import com.system.file.entity.FsFile;

/**
 * 文件管理
 * @author fyx
 *
 */
public interface FileService {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final Charset CHARSET = Charset.forName("UTF-8");

	public ApiResponseResult upload(FsFile fsFile, MultipartFile file) throws Exception;

	public ApiResponseResult uploadByBs(FsFile fsFile, MultipartFile file,Long bsId,String bsType) throws Exception;

	
	public ApiResponseResult uploadByNameAndUrl(String file_name,String url,FsFile fsFile, MultipartFile file) throws Exception;

	public ApiResponseResult get(Long fsFileId, HttpServletResponse response) throws Exception;

	public ApiResponseResult getListByBs(Long mid,String bsType, PageRequest pageRequest) throws Exception;

	public ApiResponseResult onlineView(Long fsFileId, HttpServletResponse response) throws Exception;

	public ApiResponseResult delete(Long fsFileId) throws Exception;
	
	public ApiResponseResult viewByUrl(String url,String fileName, HttpServletResponse response) throws Exception;

}
