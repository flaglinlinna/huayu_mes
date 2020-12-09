package com.web.basePrice.service;

import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.ItemTypeWg;
import org.springframework.data.domain.PageRequest;


public interface ItemTypeWgService {

  public ApiResponseResult add(ItemTypeWg itemTypeWg) throws Exception;

  public ApiResponseResult edit(ItemTypeWg itemTypeWg) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

}