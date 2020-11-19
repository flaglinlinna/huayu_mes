package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.produce.service.InputCheckService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投料校验模块
 *
 */
@Service(value = "InputCheckService")
@Transactional(propagation = Propagation.REQUIRED)
public class InputCheckImpl extends PrcUtils implements InputCheckService {

    @Override
    public ApiResponseResult getTaskNo(String keyword) throws Exception {
        List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",1,UserUtil.getSessionUser().getId()+"",keyword);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
    }

    @Override
    public ApiResponseResult getInfoBarcode(String barcode,String taskNo) throws Exception {
        List<Object> list = getInfoBarcodePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",taskNo,barcode,"PRC_BATCH_CHECK_BARCODE");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
    }

    @Override
    public ApiResponseResult addPut(String barcode, String task_no, String item_no, String qty) throws Exception {
        List<Object> list = addPutPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",barcode,
                task_no, item_no, qty,UserUtil.getSessionUser().getId().toString(),
                "辅料","PRC_BATCH_IMP_BARCODE");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("Qty", list.get(2));
        map.put("List", list.get(3));
        return ApiResponseResult.success().data(map);
    }

    @Override
    public ApiResponseResult delete(String id) throws Exception {
        List<Object> list = deletePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",id,UserUtil.getSessionUser().getId(),"PRC_BATCH_DEL_BARCODE");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success(list.get(1).toString()).data(list.get(2));
    }

    @Override
    public ApiResponseResult getDetailByTask(String taskNo) throws Exception {
        List<Object> list = getDetailByTaskPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",4,UserUtil.getSessionUser().getId()+"",taskNo);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
    }

    @Override
    public ApiResponseResult getHistoryList(String keyword, String hStartTime, String hEndTime, PageRequest pageRequest) throws Exception {
        List<Object> list = getHistoryPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
                hStartTime,hEndTime,keyword,
                pageRequest.getPageNumber()+1,pageRequest.getPageSize(),"prc_BATCH_SETUP_INFO_DETAIL");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }
}
