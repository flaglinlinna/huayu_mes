package com.web.basePrice.service;

import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.CustomQs;
import org.springframework.data.domain.PageRequest;


public interface CustomQsService {

  public ApiResponseResult add(CustomQs customQs) throws Exception;

  public ApiResponseResult edit(CustomQs customQs) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult delFile(Long id) throws Exception;

  public ApiResponseResult getQsType(String keyword,PageRequest pageRequest) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
//  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变
  
//  public ApiResponseResult getUnitList()throws Exception;

}