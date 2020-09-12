package com.web.po.service.internal;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.app.base.data.ApiResponseResult;
import com.web.po.ApiUtil;
import com.web.po.service.PoService;

@Service(value = "PoService")
@Transactional(propagation = Propagation.REQUIRED)
public class PoImpl  implements PoService {

	@Override
	public ApiResponseResult findProductionPOBoardData(String string) throws Exception {
		// TODO Auto-generated method stub
		HashMap map = new HashMap();
		map.put("detail", JSON.parseObject(ApiUtil.getList("https://api-beta.huawei.com:443/service/esupplier/findProductionPOBoardData/1.0.0","{}"), HashMap.class));
		map.put("toBeAccepted", JSON.parseObject(ApiUtil.getList("https://api-beta.huawei.com:443/service/esupplier/findPoLineList/1.0.0/20/1","{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"all\",\"statusType\":\"COL_TASK_STATUS\"}"), HashMap.class));
		map.put("accepted", JSON.parseObject(ApiUtil.getList("https://api-beta.huawei.com:443/service/esupplier/findPoLineList/1.0.0/20/1","{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"signed_back\",\"colTaskOrPoStatus\":\"all\",\"statusType\":\"COL_TASK_STATUS\"}"), HashMap.class));
		return ApiResponseResult.success("").data(map);
	}

	@Override
	public ApiResponseResult findPoLineList(String type,int page,int rows) throws Exception {
		// TODO Auto-generated method stub
		if("0".equals(String.valueOf(page)) || "null".equals(String.valueOf(page)) || page <= 0){
			page = 1;
		}
		if("0".equals(String.valueOf(rows)) || "null".equals(String.valueOf(rows)) || rows <= 0){
			rows = 20;
		}
		String param = "{}";
		if(type.equals("newOrderQuantity")){//新订单
			param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"huaweiPublishOrder\",\"statusType\":\"COL_TASK_STATUS\",\"includeVCICA\":-1}";
		}
		if(type.equals("deliveryTimeChangedOrderQuantity")){//订单交期修改
		    param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"huaweiApplyRequredDateChange\",\"statusType\":\"COL_TASK_STATUS\"}";
        }
        if(type.equals("cancelledToConfirmedOrderQuantity")){//订单取消确认
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"huaweiApplyCancelOrder\",\"statusType\":\"COL_TASK_STATUS\"}";
        }
        if(type.equals("changedOrderQuantity")){//内容变更通知
            param = "{}";
        }
        if(type.equals("cancelOrderQuantity")){//订单取消通知
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"huaweiNotifyCancelOrder\",\"statusType\":\"COL_TASK_STATUS\"}";
        }
        if(type.equals("deliveryTimeChangedPendingOrderQuantity")){//待华为确认交期更改
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"vendorApplyPromiseDateChange\",\"statusType\":\"COL_TASK_STATUS\"}";
        }
        if(type.equals("vendorApplyQtyChangeOrderQuantity")){//待华为确认数量变更
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"vendorApplyQuantityChange\",\"statusType\":\"COL_TASK_STATUS\"}";
        }
        if(type.equals("vendorApplyCancelOrderQuantity")){//待华为确认取消
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"vendorApplyCancelOrder\",\"statusType\":\"COL_TASK_STATUS\"}";
        }
        if(type.equals("rmaBarterOrderQuantity")){//RMA交货订单通知
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"huaweiNotifyRMAChange\",\"statusType\":\"COL_TASK_STATUS\"}";
        }
        if(type.equals("newPOVciCAQuantity")){//VCI-CA订单
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"huaweiPublishOrder\",\"statusType\":\"COL_TASK_STATUS\",\"includeVCICA\":1}";
        }

        if(type.equals("intransitOrderQuantity")){//在途订单
		    param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"signed_back\",\"colTaskOrPoStatus\":\"OPEN\",\"statusType\":\"PO_STATUS\"}";
        }
        if(type.equals("closed4Receving")){//已交货未付款关闭
            param = "{\"poSubType\": \"P\",\"poStatus\": \"signed_back\",\"colTaskOrPoStatus\": \"CLOSED FOR RECEVING\",\"statusType\": \"PO_STATUS\"}";
        }
        if(type.equals("closedOrderQuantity")){//已付款关闭
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"signed_back\",\"colTaskOrPoStatus\":\"CLOSED\",\"statusType\":\"PO_STATUS\"}";
        }
        if(type.equals("archivedOrder")){//归档订单
            param = "{\"instanceId\":\"1\",\"poHeaderId\":\"87327647\",\"poReleaseId\":\"2079606\",\"calculateOrderAmount\":true}";
        }
        if(type.equals("cancelledOrderQuantity")){//取消订单
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"signed_back\",\"colTaskOrPoStatus\":\"CANCELLED\",\"statusType\":\"PO_STATUS\"}";
        }

        if(type.equals("expiredOrderQuantity")){//新订单超过三天未处理
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"warn\",\"colTaskOrPoStatus\":\"new_po_undeal_more_than_3_days\",\"statusType\":\"WARN\"}";
        }
        if(type.equals("arrivalOnWeekOrderQuantity")){//预计两周内到货
            param = " {\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"warn\",\"colTaskOrPoStatus\":\"po_arrive_in_2_weeks\",\"statusType\":\"WARN\"}";
        }
        if(type.equals("nonDeliveryOrderQuantity")){//下单六个月未交货
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"warn\",\"colTaskOrPoStatus\":\"po_no_arrive_more_than_6_monthes\",\"statusType\":\"WARN\"}";
        }
        if(type.equals("overdueOrderQuantity")){//过期PO
            param = "{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"warn\",\"colTaskOrPoStatus\":\"po_warning_overdue\",\"statusType\":\"WARN\"}";
        }

		return ApiResponseResult.success("").data(JSON.parseObject(ApiUtil.getList("https://api-beta.huawei.com:443/service/esupplier/findPoLineList/1.0.0/"+rows+"/"+page+"",param), HashMap.class));
	}

}
