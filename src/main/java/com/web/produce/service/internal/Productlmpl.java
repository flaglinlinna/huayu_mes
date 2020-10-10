package com.web.produce.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public ApiResponseResult getHXTaskNo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",5,UserUtil.getSessionUser().getId()+"",keyword);
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
		return ApiResponseResult.success("");
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

	
}
