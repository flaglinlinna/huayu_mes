package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.PriceComm;

/**
 *
 * @date Dec 7, 2020 5:05:31 PM
 */
public interface PriceCommService {

  public ApiResponseResult add(PriceComm priceComm) throws Exception;

  public ApiResponseResult edit(PriceComm priceComm) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

}