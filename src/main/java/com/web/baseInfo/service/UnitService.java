package com.web.baseInfo.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.baseInfo.entity.Unit;

/**s
 *
 * @date Nov 4, 2020 4:27:53 PM
 */
public interface UnitService {

  public ApiResponseResult add(Unit unit) throws Exception;

  public ApiResponseResult edit(Unit unit) throws Exception;

  // 根据ID获取详情
  public ApiResponseResult getUnit(Long id) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

}