package com.system.file.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.file.entity.FsFile;
import com.system.file.service.FileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 文件管理
 * @author fyx
 *
 */
@Api(description = "Qms文件管理")
@RestController
@RequestMapping(value="/file")
public class FileController extends WebController {
	@Autowired
	private FileService fileService;

	@ApiOperation(value="上传文件", notes="上传文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "附件", dataType = "MultipartFile", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ApiResponseResult upload(MultipartFile file) {
		try {
			FsFile fsFile = new FsFile();
			return fileService.upload(fsFile, file);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value="下载文件", notes="下载文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fsFileId", value = "文件ID", required = true, dataType = "Long", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void get(@RequestParam(value = "fsFileId", required = true) Long fsFileId) {
		try {
			fileService.get(fsFileId, getResponse());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@ApiOperation(value="图片在线预览", notes="图片在线预览")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fsFileId", value = "文件ID", required = true, dataType = "Long", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void view(@RequestParam(value = "fsFileId", required = true) Long fsFileId) {
		try {
			fileService.onlineView(fsFileId, getResponse());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

    @ApiOperation(value="删除文件", notes="删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fsFileId", value = "文件ID", required = true, dataType = "Long", paramType="query",defaultValue=""),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ApiResponseResult delete(@RequestParam(value = "fsFileId", required = true) Long fsFileId){
	    try{
	        return fileService.delete(fsFileId);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ApiResponseResult.failure(e.getMessage());
        }
    }
}
