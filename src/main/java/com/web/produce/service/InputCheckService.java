package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import org.springframework.data.domain.PageRequest;

/**
 * 投料校验模块
 */
public interface InputCheckService {

    public ApiResponseResult getTaskNo(String keyword) throws Exception;

    public ApiResponseResult getInfoBarcode(String barcode) throws Exception;

    public ApiResponseResult addPut(String barcode, String task_no, String item_no, String qty) throws Exception;

    public ApiResponseResult delete(String id) throws Exception;

    public ApiResponseResult getDetailByTask(String taskNo) throws Exception;

    public ApiResponseResult getHistoryList(String keyword, String hStartTime, String hEndTime, PageRequest pageRequest) throws Exception;
}
