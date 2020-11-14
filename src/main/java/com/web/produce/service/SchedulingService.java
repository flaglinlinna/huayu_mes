package com.web.produce.service;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.Scheduling;
import com.web.produce.entity.SchedulingItem;
import com.web.produce.entity.SchedulingProcess;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 排产信息
 */
public interface SchedulingService {

    public ApiResponseResult add(Scheduling scheduling) throws Exception;

    public ApiResponseResult edit(Scheduling scheduling) throws Exception;

    public ApiResponseResult delete(Long id) throws Exception;

    public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

    public ApiResponseResult getListByProcedure(String keyword, PageRequest pageRequest) throws Exception;

    public ApiResponseResult getSchedulData(Long id) throws Exception;

    public ApiResponseResult getExcel(HttpServletResponse response) throws Exception;

    public ApiResponseResult doExcel(MultipartFile file) throws Exception;

    public ApiResponseResult doCheckProc() throws Exception;

    public ApiResponseResult getTempList(PageRequest pageRequest) throws Exception;

    public ApiResponseResult deleteTempAll() throws Exception;

    public ApiResponseResult confirmTemp() throws Exception;

    //提取工序存储过程
    public ApiResponseResult getProcessProc() throws Exception;

    //获取生产制令单从表-工艺
    public ApiResponseResult getProcessList(String keyword, Long mid, PageRequest pageRequest) throws Exception;

    public ApiResponseResult editProcess(SchedulingProcess schedulingProcess) throws Exception;

    public ApiResponseResult saveProcessProc(Long mid, String processIds) throws Exception;

    //获取生产制令单从表-组件
    public ApiResponseResult getItemList(String keyword, Long mid, PageRequest pageRequest) throws Exception;

    //获取人员列表
    public ApiResponseResult getEmpList(Long mid, PageRequest pageRequest) throws Exception;

    //获取生产投料
    public ApiResponseResult getProdOrderList(Long mid, PageRequest pageRequest) throws Exception;

    //获取产出送检
    public ApiResponseResult getProdOrderOutList(Long mid, PageRequest pageRequest) throws Exception;

    //获取品质检验列表
    public ApiResponseResult getProdOrderQcList(Long mid, PageRequest pageRequest) throws Exception;

    public ApiResponseResult editItem(SchedulingItem schedulingItem) throws Exception;
}
