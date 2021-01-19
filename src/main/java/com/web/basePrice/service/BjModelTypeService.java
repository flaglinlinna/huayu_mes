package com.web.basePrice.service;

import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.BjModelType;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @date Dec 8, 2020 11:27:53 AM
 */
public interface BjModelTypeService {

  public ApiResponseResult add(BjModelType bjModelType) throws Exception;

  public ApiResponseResult edit(BjModelType bjModelType) throws Exception;

  public ApiResponseResult doExcel(MultipartFile[] file) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

  public ApiResponseResult getProcList(String type, String condition, PageRequest pageRequest)throws Exception;//工序

  public ApiResponseResult getType(String keyword, PageRequest pageRequest)throws Exception;//机台类型

  public ApiResponseResult getWorkCenterList(String type, String condition, PageRequest pageRequest)throws Exception;//工作中心

  public ApiResponseResult doCheckInfo(String type, String input1, String input2, String input3, String input4)throws Exception;//检验工序&工作中心
}