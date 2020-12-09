package com.web.basePrice.service;

import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ProfitProd;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @date Dec 7, 2020 5:05:31 PM
 */
public interface ProfitProdService {

  public ApiResponseResult add(ProfitProd priceComm) throws Exception;

  public ApiResponseResult edit(ProfitProd priceComm) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变
  
//  public ApiResponseResult getUnitList()throws Exception;

}