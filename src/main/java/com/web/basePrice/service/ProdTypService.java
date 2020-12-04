package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ProdTyp;

/**s
 *
 * @date Dec 4, 2020 5:27:53 PM
 */
public interface ProdTypService {

  public ApiResponseResult add(ProdTyp prodTyp) throws Exception;

  public ApiResponseResult edit(ProdTyp prodTyp) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

}