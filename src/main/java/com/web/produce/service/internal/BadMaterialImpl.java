package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.produce.service.BadMaterialService;
import com.web.produce.service.InputCheckService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 来料不良模块
 *
 */
@Service(value = "BadMaterialService")
@Transactional(propagation = Propagation.REQUIRED)
public class BadMaterialImpl extends PrcUtils implements BadMaterialService {
//getReworkItemPrc

    @Override
    public ApiResponseResult getOrg(String keyword,PageRequest pageRequest) throws Exception{
        List<Object> list = getReworkItemPrc(UserUtil.getSessionUser().getCompany()+"",
                UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","原材料半成品",keyword,pageRequest);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    @Override
    public ApiResponseResult getDept() throws  Exception{
        List<Object> list = getDeptPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                "","来料部门", "prc_mes_cof_org_chs");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success("").data(list.get(3));
    }

    @Override
    public ApiResponseResult getSupplier(String keyword,PageRequest pageRequest)  throws  Exception{
        List<Object> list = getSupplierPrc(UserUtil.getSessionUser().getCompany()+"",
                UserUtil.getSessionUser().getFactory()+"",1,keyword,pageRequest);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    @Override
    public ApiResponseResult getBadList(String company,String factory,String keyword) throws Exception {
        // TODO Auto-generated method stub
        List<Object> list = getBadSelectPrc(company,factory,keyword);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
    }

    @Override
    public ApiResponseResult getTaskNo(String keyword) throws Exception {
        List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",4,UserUtil.getSessionUser().getId()+"",keyword);
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
    public ApiResponseResult saveMaterial(String itemNo,Integer deptId, Integer venderId,String barcode,String prodDate,
                                    String lotNo,String defectCode,String defectQty,String taskNo) throws Exception {
        List<Object> list = saveMaterialPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",
                UserUtil.getSessionUser().getId()+"", barcode,itemNo, deptId, venderId,prodDate, lotNo,defectCode,defectQty,taskNo);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
    }

    @Override
    public ApiResponseResult getInfoBarcode(String barcode) throws Exception {
        // TODO Auto-generated method stub
        List<Object> list = getItemByBarcodePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",1,barcode,"prc_mes_barcode_info_get");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
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
        List<Object> list = getMaterialHistoryPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",hStartTime,hEndTime,keyword,
                pageRequest.getPageNumber()+1,pageRequest.getPageSize(),"prc_mes_material_ng_chs");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }
}
