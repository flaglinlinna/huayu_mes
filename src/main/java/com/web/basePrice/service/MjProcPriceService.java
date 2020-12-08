package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.MjProcPrice;

/**
 *
 * @date Dec 8, 2020 5:47:26 PM
 */
public interface MjProcPriceService {

  public ApiResponseResult add(MjProcPrice mjProcPrice) throws Exception;

  public ApiResponseResult edit(MjProcPrice mjProcPrice) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

}