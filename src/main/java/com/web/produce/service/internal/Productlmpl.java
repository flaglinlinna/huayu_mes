package com.web.produce.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.produce.service.ProductService;

/**
 * 生产报工模块
 *
 */
@Service(value = "ProductService")
@Transactional(propagation = Propagation.REQUIRED)
public class Productlmpl extends PrcUtils implements ProductService {
	
	@Override
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		SysUser sy = UserUtil.getSessionUser();
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",4,UserUtil.getSessionUser().getId()+"",keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	@Override
	public ApiResponseResult getHXTaskNo(String taskNo,String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getHXTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",taskNo,keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult afterNei(String barcode, String task_no) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = afterNeiPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",barcode,
				task_no);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
        List<Object> list2 = getBarcodeByPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",
                UserUtil.getSessionUser().getId()+"",5,barcode);

        if (!list2.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list2.get(1).toString());
        }
		return ApiResponseResult.success("").data(list2.get(2));
	}

	@Override
	public ApiResponseResult afterWai(String nbarcode, String task_no, String wbarcode, String hx, String ptype)
			throws Exception {
		// TODO Auto-generated method stub
		if(ptype.equals("1")){
			ptype="正常扫描";
		}else if(ptype.equals("2")){
			ptype="合箱扫描";
		}
		List<Object> list = afterWaiPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",task_no,
				nbarcode,wbarcode,ptype,hx,UserUtil.getSessionUser().getId()+"");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map m = new HashMap();
		m.put("Qty", list.get(2));
		m.put("Rate", list.get(3));
		m.put("List", list.get(4));
		return ApiResponseResult.success().data(m);
	}

	@Override
	public ApiResponseResult getDetailByTask(String taskNo) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getDetailByTaskPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",3,UserUtil.getSessionUser().getId()+"",taskNo);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	@Override
	public ApiResponseResult getHistoryList(String keyword, String hStartTime, String hEndTime, PageRequest pageRequest)
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getHistoryPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				hStartTime,hEndTime,keyword,
				pageRequest.getPageNumber()+1,pageRequest.getPageSize(),"PRC_MES_BOARD_WORK_REC");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	
}
