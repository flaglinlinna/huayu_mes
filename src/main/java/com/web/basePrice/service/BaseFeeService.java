package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.BaseFee;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @date Dec 8, 2020 11:27:53 AM
 */
public interface BaseFeeService {

  public ApiResponseResult add(BaseFee baseFee) throws Exception;

  public ApiResponseResult edit(BaseFee baseFee) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

  public ApiResponseResult doExcel(MultipartFile[] file) throws Exception;

  public void exportExcel(HttpServletResponse response, String keyword) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变
  
  public ApiResponseResult getProcList(String type,String condition,PageRequest pageRequest)throws Exception;//工序
  
  public ApiResponseResult getType(String keyword ,PageRequest pageRequest)throws Exception;//机台类型

  public ApiResponseResult getWorkCenterList(String type,String condition,PageRequest pageRequest)throws Exception;//工作中心
  
  public ApiResponseResult doCheckInfo(String type,String input1,String input2,String input3,String input4)throws Exception;//检验工序&工作中心

  public ApiResponseResult getFileList(Long customId) throws Exception;

  public ApiResponseResult delFile(Long id,Long fileId) throws Exception;
}