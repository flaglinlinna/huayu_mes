package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import org.springframework.data.domain.PageRequest;

/**
 * 投料校验模块
 */
public interface BadMaterialService {

    //物料编码查询框
    public ApiResponseResult getOrg(String keyword,PageRequest pageRequest) throws Exception;

    //来料部门下拉
    public ApiResponseResult getDept() throws  Exception;

    //供应商查询框
    public ApiResponseResult getSupplier(String keyword,PageRequest pageRequest)  throws  Exception;

    //不良内容下拉框
    public ApiResponseResult getBadList(String company,String factory,String keyword) throws Exception;

    public ApiResponseResult getTaskNo(String keyword) throws Exception;

    public ApiResponseResult getInfoBarcode(String barcode, String taskNo) throws Exception;

    public ApiResponseResult saveMaterial(String itemNo,Integer deptId, Integer venderId,String prodDate,
                                          String lotNo,String defectCode,String defectQty) throws Exception;


    public ApiResponseResult getInfoBarcode(String barcode) throws Exception;

    public ApiResponseResult delete(String id) throws Exception;

    public ApiResponseResult getDetailByTask(String taskNo) throws Exception;

    public ApiResponseResult getHistoryList(String keyword, String hStartTime, String hEndTime, PageRequest pageRequest) throws Exception;
}
