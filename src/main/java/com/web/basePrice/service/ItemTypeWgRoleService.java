package com.web.basePrice.service;

import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ItemTypeWgRole;
import org.springframework.data.domain.PageRequest;


public interface ItemTypeWgRoleService {

  public ApiResponseResult add(ItemTypeWgRole itemTypeWgRole) throws Exception;

  public ApiResponseResult add(Long pkItemTypeWg,String roleIds) throws Exception;

  public ApiResponseResult edit(ItemTypeWgRole itemTypeWgRole) throws Exception;

  public ApiResponseResult getByWgId(Long wgId) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

}