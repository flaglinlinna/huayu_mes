package com.system.defect.service;

import com.app.base.data.ApiResponseResult;
import com.system.defect.entity.SysDefect;
import org.springframework.data.domain.PageRequest;

/**
 * 缺陷记录
 */
public interface SysDefectService {

    public ApiResponseResult add(SysDefect sysDefect) throws Exception;

    public ApiResponseResult edit(SysDefect sysDefect) throws Exception;

    public ApiResponseResult delete(Long id) throws Exception;

    public ApiResponseResult getList(String keyword, Integer priority, String status, PageRequest pageRequest) throws Exception;

    //获取附件管理列表
    public ApiResponseResult getFile(Long defectId) throws Exception;

    //附件管理上传文件
    public ApiResponseResult addFile(Long defectId, Long fileId, String fileName) throws Exception;

    //附件管理删除文件
    public ApiResponseResult delFile(Long defectId, Long fileId) throws Exception;
}
