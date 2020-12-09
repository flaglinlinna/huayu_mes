package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.Proc;

/**s
 *
 * @date Dec 4, 2020 3:22:35 PM
 */
public interface ProcService {

  public ApiResponseResult add(Proc proc) throws Exception;

  public ApiResponseResult edit(Proc proc) throws Exception;

  // 根据ID获取详情
  public ApiResponseResult getProc(Long id) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变
  
  public ApiResponseResult getWorkCenterList(String type,String condition,PageRequest pageRequest)throws Exception;//工作中心

}