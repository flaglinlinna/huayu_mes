package com.web.produce.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.UserUtil;
import com.web.produce.service.InputService;

/**
 * 生产投料模块
 *
 */
@Service(value = "InputService")
@Transactional(propagation = Propagation.REQUIRED)
public class Inputlmpl extends PrcUtils implements InputService {
	
	@Override
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",1,UserUtil.getSessionUser().getId()+"",keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getInfoBarcode(String barcode) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getInfoBarcodePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",barcode,"PRC_BATCH_CHECK_BARCODE");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult addPut(String barcode, String task_no, String item_no, String qty) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = addPutPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",barcode,
				task_no, item_no, qty,UserUtil.getSessionUser().getUserCode(),
				"主料","PRC_BATCH_IMP_BARCODE");
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
		// TODO Auto-generated method stub
		List<Object> list = deletePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",id,UserUtil.getSessionUser().getId(),"PRC_BATCH_DEL_BARCODE");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success(list.get(1).toString()).data(list.get(2));
	}

	@Override
	public ApiResponseResult getDetailByTask(String taskNo) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getDetailByTaskPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",1,UserUtil.getSessionUser().getId()+"",taskNo);
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
