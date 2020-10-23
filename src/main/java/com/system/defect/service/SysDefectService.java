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
}
