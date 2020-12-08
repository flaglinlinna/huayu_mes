package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.BaseFee;

/**
 *
 * @date Dec 8, 2020 11:27:53 AM
 */
public interface BaseFeeService {

  public ApiResponseResult add(BaseFee baseFee) throws Exception;

  public ApiResponseResult edit(BaseFee baseFee) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变
  
  public ApiResponseResult getProcList(String type,String condition,PageRequest pageRequest)throws Exception;//工序
  
  public ApiResponseResult getType(String keyword ,PageRequest pageRequest)throws Exception;//机台类型

}