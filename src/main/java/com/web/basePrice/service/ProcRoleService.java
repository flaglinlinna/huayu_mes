package com.web.basePrice.service;

import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ProcRole;
import org.springframework.data.domain.PageRequest;


public interface ProcRoleService {

  public ApiResponseResult add(ProcRole procRole) throws Exception;

  public ApiResponseResult add(Long pkProc,String roleIds) throws Exception;

  public ApiResponseResult edit(ProcRole procRole) throws Exception;

  public ApiResponseResult getByWgId(Long wgId) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

}